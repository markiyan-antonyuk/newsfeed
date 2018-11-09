package com.markantoni.newsfeed.repository

import com.markantoni.newsfeed.repository.model.Article
import com.markantoni.newsfeed.repository.network.NetworkRepository
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class NewsFeedRepository : Repository, KoinComponent {
    private val networkRepository by inject<NetworkRepository>()
    //offline repo

    override suspend fun loadArticles(pageSize: Int, page: Int): List<Article> =
//        try {
        networkRepository.loadArticles(pageSize, page)
//        } catch (e: UnknownHostException, HttpException) { //no network
//            listOf() //offline repo load
//        }

    override suspend fun loadArticle(id: String): Article =
            networkRepository.loadArticle(id)
}