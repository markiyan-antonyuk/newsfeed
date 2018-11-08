package com.markantoni.newsfeed.repository

import com.markantoni.newsfeed.repository.model.Article

interface Repository {
    companion object {
        const val PAGE_SIZE = 25
    }

    suspend fun loadArticles(page: Int): List<Article>
}

//this should everything be in separate module, and all classes except this one should be internal