package com.markantoni.newsfeed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.markantoni.newsfeed.datasource.ArticlesDataSourceFactory
import com.markantoni.newsfeed.repository.model.Article
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class NewsFeedViewModel : CoroutineViewModel(), KoinComponent {
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
}