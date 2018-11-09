package com.markantoni.newsfeed.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.markantoni.newsfeed.R
import com.markantoni.newsfeed.extensions.inflate
import com.markantoni.newsfeed.extensions.loadImage
import com.markantoni.newsfeed.extensions.showErrorSnackbar
import com.markantoni.newsfeed.repository.model.Article
import com.markantoni.newsfeed.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_article_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleDetailsFragment : Fragment() {
    companion object {
        private const val KEY_ARTICLE = "key.article"
        fun newInstance(article: Article) = ArticleDetailsFragment().apply {
            arguments = bundleOf(KEY_ARTICLE to article)
        }
    }

    private val viewModel by viewModel<ArticleViewModel>()
    private lateinit var article: Article

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        container?.inflate(R.layout.fragment_article_details)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindArticle(arguments?.getParcelable(KEY_ARTICLE) ?: error("Must pass an article"), true)
        articleSaveBtn.setOnClickListener { viewModel.saveArticle(article) }
        articleImage.transitionName = article.title

        viewModel.article.observe(this, Observer { bindArticle(it) })
        viewModel.error.observe(this, Observer { view?.showErrorSnackbar { viewModel.loadArticle(article.id) } })
        viewModel.loadArticle(article.id)
    }

    private fun bindArticle(article: Article, firstBind: Boolean = false) {
        this.article = article

        articleTitle.text = article.title
        articleCategory.text = article.category
        articleDescription.isVisible = article.description?.let {
            articleDescription.text = it
            true
        } ?: false
        articleImage.loadImage(article.image, false)
        if (!firstBind) articleSaveBtn.isVisible = !article.isSaved
    }
}