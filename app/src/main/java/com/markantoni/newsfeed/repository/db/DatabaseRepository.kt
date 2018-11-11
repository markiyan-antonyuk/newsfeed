package com.markantoni.newsfeed.repository.db

import com.markantoni.newsfeed.repository.Repository
import com.markantoni.newsfeed.repository.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class DatabaseRepository : Repository, KoinComponent, CoroutineScope {
    override val coroutineContext = GlobalScope.coroutineContext
    private val db by inject<ArticlesDatabase>()

    override suspend fun loadArticles(pageSize: Int, page: Int): List<Article> = async {
        if (page == 1) {
            db.articlesDao().selectAllArticles().sortedByDescending { it.timestamp }.map { it.toArticle() }
        } else {
            listOf()
        }
    }.await()

    override suspend fun loadArticle(id: String): Article = async {
        db.articlesDao().findArticle(id)?.toArticle() ?: error("Item can't be fetched from DB")
    }.await()

    override suspend fun saveArticle(article: Article) {
        launch { db.articlesDao().saveArticle(article.toArticleModel()) }
    }

    override suspend fun deleteArticle(article: Article) {
        launch { db.articlesDao().deleteArticle(article.toArticleModel()) }
    }

    fun updateArticle(article: Article) {
        launch { db.articlesDao().updateArticle(article.toArticleModel()) }
    }
}