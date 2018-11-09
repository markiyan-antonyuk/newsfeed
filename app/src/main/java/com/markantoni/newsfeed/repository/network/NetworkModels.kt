package com.markantoni.newsfeed.repository.network

import com.google.gson.annotations.SerializedName
import com.markantoni.newsfeed.extensions.toTimestamp
import com.markantoni.newsfeed.repository.model.Article

data class ArticlesResponse(val response: Response)
data class Response(val currentPage: Int, val pages: Int, val results: List<Result>)
data class Result(
    val id: String, @SerializedName("sectionName") val category: String?,
    @SerializedName("webPublicationDate") val date: String?,
    @SerializedName("webTitle") val title: String?, val fields: Fields?
)

data class Fields(val thumbnail: String?, @SerializedName("bodyText") val description: String?)

fun Result.toArticle() = Article(
    id, title ?: "", category ?: "",
    fields?.thumbnail ?: "", fields?.description ?: "",
    date?.toTimestamp() ?: System.currentTimeMillis()
)