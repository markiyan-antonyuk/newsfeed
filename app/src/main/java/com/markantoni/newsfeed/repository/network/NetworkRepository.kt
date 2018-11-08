package com.markantoni.newsfeed.repository.network

import com.markantoni.newsfeed.repository.model.Article
import com.markantoni.newsfeed.repository.Repository
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class NetworkRepository : Repository, KoinComponent {
    private val guardianService by inject<GuardianService>()
    private val apiKey by inject<String>("api-key")

    override suspend fun loadArticles(page: Int): List<Article> {
        val response = guardianService.fetchArticles(apiKey, page, Repository.PAGE_SIZE).await().response
        return response.results.map { Article(it.id, it.title, it.category, it.fields.thumbnail) }
    }
}