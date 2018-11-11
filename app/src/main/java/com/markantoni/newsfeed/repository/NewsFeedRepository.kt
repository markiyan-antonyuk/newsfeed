package com.markantoni.newsfeed.repository

import com.markantoni.newsfeed.repository.db.DatabaseRepository
import com.markantoni.newsfeed.repository.model.Article
import com.markantoni.newsfeed.repository.network.NetworkRepository
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.HttpException
import java.net.UnknownHostException

class NewsFeedRepository : Repository, KoinComponent {
    private val networkRepository by inject<NetworkRepository>()
    private val dbRepository by inject<DatabaseRepository>()

    override suspend fun loadArticles(pageSize: Int, page: Int): List<Article> =
        try {
            networkRepository.loadArticles(pageSize, page)
        } catch (e: Exception) {
            if (e.isNetworkRelated()) {
                dbRepository.loadArticles(pageSize, page)
            } else {
                throw e
            }
        }

    override suspend fun loadArticle(id: String): Article =
        try {
            val article = networkRepository.loadArticle(id)
            try {
                //check if it's saved too
                val dbArticle = dbRepository.loadArticle(id)
                val saved = article.id == dbArticle.id
                if (saved) dbRepository.updateArticle(article)
                article.isSaved = saved
            } catch (e: Exception) {
            }
            article
        } catch (e: Exception) {
            if (e.isNetworkRelated()) {
                dbRepository.loadArticle(id)
            } else {
                throw e
            }
        }

    override suspend fun saveArticle(article: Article) = dbRepository.saveArticle(article)

    override suspend fun deleteArticle(article: Article) = dbRepository.deleteArticle(article)

    private fun Exception.isNetworkRelated() = this is UnknownHostException || this is HttpException
}