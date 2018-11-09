package com.markantoni.newsfeed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.markantoni.newsfeed.datasource.ArticlesDataSourceFactory
import com.markantoni.newsfeed.repository.model.Article
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class NewsFeedViewModel : ViewModel(), CoroutineScope, KoinComponent {
    private val job = Job()
    override val coroutineContext = Dispatchers.Main + job

    private val dataSourceFactory by inject<ArticlesDataSourceFactory>()
    val articles: LiveData<PagedList<Article>>
    val articlesError = Transformations.switchMap(dataSourceFactory.dataSource) { it.error }
    val articlesLoading = Transformations.switchMap(dataSourceFactory.dataSource) { it.loading }

    init {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(25)
            .setPageSize(10)
            .build()

        articles = LivePagedListBuilder<Int, Article>(dataSourceFactory, config).build()
    }

    fun reloadArticles() = articles.value?.dataSource?.invalidate()

    override fun onCleared() = job.cancel()
}