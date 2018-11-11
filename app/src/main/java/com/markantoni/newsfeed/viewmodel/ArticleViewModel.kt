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

    private val _pinnedArticles = mutableListOf<Article>()
    val pinnedArticles = MutableLiveData<List<Article>>()

    fun loadArticle(id: String) = launch { article.value = repository.loadArticle(id) }

    fun saveArticle(article: Article) = launch {
        article.isSaved = true
        repository.saveArticle(article)
        this@ArticleViewModel.article.value = article
    }

    fun deleteArticle(article: Article) = launch {
        article.isSaved = false
        repository.deleteArticle(article)
        this@ArticleViewModel.article.value = article
    }

    fun pinArticle(article: Article) {
        val index = _pinnedArticles.indexOfFirst { article.id == it.id }
        if (index != -1) _pinnedArticles.removeAt(index)
        _pinnedArticles.add(article)
        pinnedArticles.value = _pinnedArticles
    }

    fun unpinArticle(article: Article) {
        val index = _pinnedArticles.indexOfFirst { article.id == it.id }
        if (index != -1) _pinnedArticles.removeAt(index)
        pinnedArticles.value = _pinnedArticles
    }

    fun isPinned(article: Article) = _pinnedArticles.indexOfFirst { article.id == it.id } != -1
}