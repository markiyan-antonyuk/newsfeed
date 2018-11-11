package com.markantoni.newsfeed.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.markantoni.newsfeed.MainActivity
import com.markantoni.newsfeed.R

object NotificationManager {
    private const val NOTIFICATION_CHANNEL_ID = "newsfeed"
    private const val NOTIFICATION_ID = 341

    fun showArticlesAvailableNotification(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val activityIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notification = NotificationCompat
            .Builder(context, getChannelId(context))
            .setSmallIcon(R.drawable.ic_news)
            .setContentTitle(context.getString(R.string.notification_articles_available))
            .setContentText(context.getString(R.string.notification_click_to_read))
            .setContentIntent(activityIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private fun getChannelId(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            context.getSystemService<NotificationManager>()?.createNotificationChannel(channel)
            channel.id
        } else {
            NOTIFICATION_CHANNEL_ID
        }
}