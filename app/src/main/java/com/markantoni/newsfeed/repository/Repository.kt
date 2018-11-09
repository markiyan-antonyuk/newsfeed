package com.markantoni.newsfeed.repository

import com.markantoni.newsfeed.repository.model.Article

interface Repository {
    suspend fun loadArticles(pageSize: Int, page: Int): List<Article>
    suspend fun loadArticle(id: String): Article
    suspend fun saveArticle(article: Article)
}

//this should everything be in separate module, and all classes except this one should be internal