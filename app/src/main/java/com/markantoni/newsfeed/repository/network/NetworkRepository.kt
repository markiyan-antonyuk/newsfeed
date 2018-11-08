package com.markantoni.newsfeed.repository.network

import com.markantoni.newsfeed.model.Article
import com.markantoni.newsfeed.repository.Repository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class NetworkRepository : Repository, KoinComponent {
    private val guardianService by inject<GuardianService>()
    private val apiKey by inject<String>("api-key")

    override suspend fun loadArticles(page: Int): List<Article> = GlobalScope.async {
        val response = guardianService.fetchArticles(apiKey, page, Repository.PAGE_SIZE).await().response
        response.results.map { Article(it.title, it.category, it.fields.thumbnail) }
    }.await()
}