package com.markantoni.newsfeed.viewmodel

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.markantoni.newsfeed.SingleLiveData
import com.markantoni.newsfeed.repository.model.Article

//nice to use such viewmodel instead of default master details flow, and it's highly testable as fragments and activity are not coupled by any interface
class NavigationViewModel : ViewModel() {
    val requestedShowArticleDetails = SingleLiveData<ArticleTransition>()

    fun requestShowArticleDetails(article: Article, image: ImageView) {
        requestedShowArticleDetails.value = ArticleTransition(article, image)
    }

    data class ArticleTransition(val article: Article, val image: ImageView)
}