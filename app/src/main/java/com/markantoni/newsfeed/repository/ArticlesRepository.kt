package com.markantoni.newsfeed.repository

import com.markantoni.newsfeed.model.Article
import com.markantoni.newsfeed.repository.network.NetworkRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.net.UnknownHostException

class ArticlesRepository : Repository, KoinComponent {
    private val networkRepository by inject<NetworkRepository>()
    //offline repo

    override suspend fun loadArticles(page: Int): List<Article> = GlobalScope.async {
        try {
            networkRepository.loadArticles(page)
        } catch (e: UnknownHostException) { //no network
            listOf<Article>() //offline repo load
        }
    }.await()
}