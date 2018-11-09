package com.markantoni.newsfeed.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.markantoni.newsfeed.MainActivity
import com.markantoni.newsfeed.R
import com.markantoni.newsfeed.repository.network.NetworkRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class NewsFeedWorker(context: Context, params: WorkerParameters) : Worker(context, params), KoinComponent {
    companion object {
        const val KEY_TIMESTAMP = "key.timestamp"
        private const val NOTIFICATION_CHANNEL_ID = "newsfeed"
        private const val NOTIFICATION_ID = 341
    }

    private val networkRepository by inject<NetworkRepository>()

    override fun doWork(): Result {
        GlobalScope.launch {
            val initialTimestamp = inputData.getLong(KEY_TIMESTAMP, System.currentTimeMillis())
            val timestamp = networkRepository.loadArticles(1, 1).first().timestamp
            if (timestamp > initialTimestamp) {
                WorkManager.getInstance().cancelAllWork()
                showArticlesAvailableNotification()
            }
        }
        return Result.SUCCESS
    }

    private fun showArticlesAvailableNotification() {
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                applicationContext.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            applicationContext.getSystemService<NotificationManager>()?.createNotificationChannel(channel)
            channel.id
        } else {
            NOTIFICATION_CHANNEL_ID
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val activityIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val notification = NotificationCompat
            .Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_news)
            .setContentTitle(applicationContext.getString(R.string.notification_articles_available))
            .setContentText(applicationContext.getString(R.string.notification_click_to_read))
            .setContentIntent(activityIntent)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
    }
}