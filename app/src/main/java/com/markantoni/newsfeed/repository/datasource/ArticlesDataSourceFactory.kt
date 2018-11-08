package com.markantoni.newsfeed.repository.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.markantoni.newsfeed.repository.model.Article

class ArticlesDataSourceFactory : DataSource.Factory<Int, Article>() {
    val dataSource = MutableLiveData<ArticlesDataSource>()

    override fun create(): DataSource<Int, Article> = ArticlesDataSource().apply { dataSource.postValue(this) }
}