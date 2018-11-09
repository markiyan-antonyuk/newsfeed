package com.markantoni.newsfeed.viewmodel

import androidx.lifecycle.MutableLiveData
import com.markantoni.newsfeed.repository.Repository
import com.markantoni.newsfeed.repository.model.Article
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class ArticleViewModel : CoroutineViewModel(), KoinComponent {
    private val repository by inject<Repository>()
    val article = MutableLiveData<Article>()

    fun loadArticle(id: String) = launch {
        article.value = repository.loadArticle(id)
    }
}