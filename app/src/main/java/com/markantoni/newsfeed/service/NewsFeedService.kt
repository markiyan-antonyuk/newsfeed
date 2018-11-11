package com.markantoni.newsfeed.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.markantoni.newsfeed.Constants
import com.markantoni.newsfeed.repository.model.Article
import com.markantoni.newsfeed.repository.network.NetworkRepository
import com.markantoni.newsfeed.util.NotificationManager
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class NewsFeedService : Service(), KoinComponent {
    companion object {
        fun newIntent(context: Context) = Intent(context, NewsFeedService::class.java)
    }

    private lateinit var job: Job
    private lateinit var afterArticle: Article
    private val networkRepository by inject<NetworkRepository>()

    override fun onCreate() {
        super.onCreate()
        job = scheduleFetchNewArticles()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int = Service.START_STICKY

    private fun scheduleFetchNewArticles() = GlobalScope.async {
        while (isActive) {
            if (!::afterArticle.isInitialized) {
                networkRepository.loadArticles(Constants.PAGE_SIZE, 1).maxBy { it.timestamp }?.let { afterArticle = it }
            } else {
                val articles = networkRepository.loadArticles(Constants.PAGE_SIZE, 1).sortedByDescending { it.timestamp }
                articles.forEach { article ->
                    if (article.id != afterArticle.id && article.timestamp > afterArticle.timestamp) {
                        afterArticle = article
                        NotificationManager.showArticlesAvailableNotification(this@NewsFeedService)
                        return@forEach
                    }
                }
            }
            delay(Constants.BACKGROUND_CHECK_INTERVAL)
        }
    }
}