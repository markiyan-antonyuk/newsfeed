package com.markantoni.newsfeed.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.markantoni.newsfeed.R
import com.markantoni.newsfeed.extensions.inflate
import com.markantoni.newsfeed.extensions.loadImage
import com.markantoni.newsfeed.repository.model.Article
import kotlinx.android.synthetic.main.fragment_article_details.*

class ArticleDetailsFragment : Fragment() {
    companion object {
        private const val KEY_ARTICLE = "key.article"
        fun newInstance(article: Article) = ArticleDetailsFragment().apply {
            arguments = bundleOf(KEY_ARTICLE to article)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        container?.inflate(R.layout.fragment_article_details)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val article by lazy { arguments?.getParcelable<Article>(KEY_ARTICLE) ?: error("Must pass an article") }
        articleImage.transitionName = article.title

        bindArticle(article)
    }

    private fun bindArticle(article: Article) {
        articleImage.loadImage(article.image, false)
    }
}