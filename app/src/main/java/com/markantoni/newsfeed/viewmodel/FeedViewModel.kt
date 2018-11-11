package com.markantoni.newsfeed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.markantoni.newsfeed.Constants
import com.markantoni.newsfeed.SingleLiveData
import com.markantoni.newsfeed.datasource.ArticlesDataSourceFactory
import com.markantoni.newsfeed.repository.model.Article
import com.markantoni.newsfeed.repository.network.NetworkRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

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
            .setInitialLoadSizeHint(Constants.INITIAL_PAGE_SIZE)
            .setPageSize(Constants.PAGE_SIZE)
            .build()

        articles = LivePagedListBuilder<Int, Article>(dataSourceFactory, config).build()
    }

    fun reloadArticles() = articles.value?.dataSource?.invalidate()

    fun scheduleCheckNewArticles(after: Article) {
        cancelScheduledCheckNewArticles()
        scheduledJob = async {
            delay(Constants.FOREGROUND_CHECK_INTERVAL)
            val articles = networkRepository.loadArticles(2, 1).sortedByDescending { it.timestamp }
            var newArticles = false
            articles.forEach {
                if (it.id != after.id && it.timestamp > after.timestamp) {
                    newArticles = true
                    cancelScheduledCheckNewArticles()
                    articlesAvailable.value = Unit
                    return@forEach
                }
            }
            if (!newArticles) scheduleCheckNewArticles(after)
        }
    }

    fun cancelScheduledCheckNewArticles() {
        scheduledJob?.cancel()
    }
}