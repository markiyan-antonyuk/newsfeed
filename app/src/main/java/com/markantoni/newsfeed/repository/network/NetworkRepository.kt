package com.markantoni.newsfeed.repository.network

import com.markantoni.newsfeed.repository.Repository
import com.markantoni.newsfeed.repository.model.Article
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class NetworkRepository : Repository, KoinComponent {
    private val guardianService by inject<GuardianService>()
    private val apiKey by inject<String>("api-key")

    override suspend fun loadArticles(pageSize: Int, page: Int): List<Article> {
        val response = guardianService.fetchArticles(apiKey, page, pageSize).await().response
        return response.results.map { it.toArticle() }
    }

    override suspend fun loadArticle(id: String): Article {
        val response = guardianService.fetchArticle(apiKey, id).await().response
        return response.results.first().toArticle()
    }

    override suspend fun saveArticle(article: Article) = error("Can't save to network")
}