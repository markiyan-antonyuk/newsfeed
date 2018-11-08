package com.markantoni.newsfeed.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.markantoni.newsfeed.model.Article
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

    val error = MutableLiveData<Throwable>()
    val articles = MutableLiveData<List<Article>>()
    val loading = MutableLiveData<Boolean>()

    init {
        launch {
            articles.value = repository.loadArticles(1)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}