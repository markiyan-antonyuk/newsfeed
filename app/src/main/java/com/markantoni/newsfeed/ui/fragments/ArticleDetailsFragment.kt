package com.markantoni.newsfeed.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        container?.inflate(R.layout.fragment_article_details)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val article by lazy { arguments?.getParcelable<Article>(KEY_ARTICLE) ?: error("Must pass an article") }
        articleImage.transitionName = article.title
        bindArticle(article)

        viewModel.article.observe(this, Observer { bindArticle(it) })
        viewModel.error.observe(this, Observer { view?.showErrorSnackbar { viewModel.loadArticle(article.id) } })
        viewModel.loadArticle(article.id)
    }

    private fun bindArticle(article: Article) {
        articleTitle.text = article.title
        articleCategory.text = article.category
        articleDescription.text = article.description
        articleImage.loadImage(article.image, false)
    }
}