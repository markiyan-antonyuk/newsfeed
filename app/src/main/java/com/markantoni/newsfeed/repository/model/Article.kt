package com.markantoni.newsfeed.repository.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
    val id: String, val title: String,
    val category: String, val image: String,
    val description: String?
) : Parcelable