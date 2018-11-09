package com.markantoni.newsfeed.repository.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface GuardianService {
    @GET("search?show-fields=thumbnail")
    fun fetchArticles(@Query("api-key") apiKey: String, @Query("page") page: Int, @Query("pageSize") pageSize: Int): Deferred<ArticlesResponse>

    @GET("search?show-fields=bodyText,thumbnail")
    fun fetchArticle(@Query("api-key") apiKey: String, @Query("ids") id: String): Deferred<ArticlesResponse>
}