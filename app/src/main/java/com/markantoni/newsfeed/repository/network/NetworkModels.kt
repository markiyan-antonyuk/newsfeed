package com.markantoni.newsfeed.repository.network

import com.google.gson.annotations.SerializedName

data class ArticlesResponse(val response: Response)
data class Response(val currentPage: Int, val pages: Int, val results: List<Result>)
data class Result(
    val id: String, @SerializedName("sectionName") val category: String,
    @SerializedName("webTitle") val title: String, val fields: Fields
)

data class Fields(val thumbnail: String)