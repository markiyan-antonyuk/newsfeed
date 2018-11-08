package com.markantoni.newsfeed.repository

import com.markantoni.newsfeed.repository.model.Article
import com.markantoni.newsfeed.repository.network.NetworkRepository
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.net.UnknownHostException

class ArticlesRepository : Repository, KoinComponent {
    private val networkRepository by inject<NetworkRepository>()
    //offline repo

    override suspend fun loadArticles(page: Int): List<Article> =
        try {
            networkRepository.loadArticles(page)
        } catch (e: UnknownHostException) { //no network
            listOf() //offline repo load
        }
}