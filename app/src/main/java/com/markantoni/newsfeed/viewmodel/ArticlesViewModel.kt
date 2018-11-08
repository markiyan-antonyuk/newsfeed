package com.markantoni.newsfeed.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.markantoni.newsfeed.SingleLiveData
import com.markantoni.newsfeed.repository.model.Article
import com.markantoni.newsfeed.repository.Repository
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class ArticlesViewModel : ViewModel(), CoroutineScope, KoinComponent {
    private val job = Job()
    override val coroutineContext = Dispatchers.Main + job + CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        error.value = throwable
    }
    private val repository by inject<Repository>()
    private var currentPage = 1
    private var _articles = mutableListOf<Article>()

    val error = SingleLiveData<Throwable>()
    val articles = MutableLiveData<List<Article>>()
    val isLoading = SingleLiveData<Boolean>()

    init {
        loadFirstPage()
    }

    fun loadFirstPage() = launch {
        isLoading.value = true
        _articles.clear()
        _articles.addAll(repository.loadArticles(1))
        articles.value = _articles
        isLoading.value = false
    }

    fun loadNextPage() {

    }

    fun reloadAllPages() {

    }

    override fun onCleared() = job.cancel()
}