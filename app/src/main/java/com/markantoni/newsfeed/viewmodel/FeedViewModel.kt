package com.markantoni.newsfeed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.markantoni.newsfeed.SingleLiveData
import com.markantoni.newsfeed.datasource.ArticlesDataSourceFactory
import com.markantoni.newsfeed.repository.model.Article
import com.markantoni.newsfeed.repository.network.NetworkRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.concurrent.TimeUnit

class FeedViewModel : CoroutineViewModel(), KoinComponent {
    private val dataSourceFactory by inject<ArticlesDataSourceFactory>()
    private val networkRepository by inject<NetworkRepository>()
    private var scheduledJob: Job? = null

    val articles: LiveData<PagedList<Article>>
    val articlesError = Transformations.switchMap(dataSourceFactory.dataSource) { it.error }
    val articlesLoading = Transformations.switchMap(dataSourceFactory.dataSource) { it.loading }
    val initialArticle = Transformations.switchMap(dataSourceFactory.dataSource) { it.initialArticle }
    val articlesAvailable = SingleLiveData<Unit>()

    init {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(25)
            .setPageSize(10)
            .build()

        articles = LivePagedListBuilder<Int, Article>(dataSourceFactory, config).build()
    }

    fun reloadArticles() = articles.value?.dataSource?.invalidate()

    fun scheduleCheckNewArticles(after: Article) {
        cancelScheduledCheckNewArticles()
        scheduledJob = async {
            delay(TimeUnit.SECONDS.toMillis(30))
            val article = networkRepository.loadArticles(1, 1).first()
            if (article.id != after.id && article.timestamp > after.timestamp) {
                cancelScheduledCheckNewArticles()
                articlesAvailable.value = Unit
            } else {
                scheduleCheckNewArticles(after)
            }
        }
    }

    fun cancelScheduledCheckNewArticles() {
        scheduledJob?.cancel()
    }
}