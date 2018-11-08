package com.markantoni.newsfeed.repository.datasource

import androidx.paging.PageKeyedDataSource
import com.markantoni.newsfeed.SingleLiveData
import com.markantoni.newsfeed.repository.Repository
import com.markantoni.newsfeed.repository.model.Article
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class ArticlesDataSource : PageKeyedDataSource<Int, Article>(), KoinComponent, CoroutineScope {
    override val coroutineContext =
        Dispatchers.Main + CoroutineExceptionHandler { _, throwable -> error.value = throwable }

    private val repository by inject<Repository>()
    val error = SingleLiveData<Throwable>()
    val loading = SingleLiveData<Boolean>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Article>) {
        launch {
            loading.value = true
            callback.onResult(repository.loadArticles(params.requestedLoadSize, 1), null, 2)
            loading.value = false
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        launch {
            callback.onResult(repository.loadArticles(params.requestedLoadSize, params.key), params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) = Unit
}