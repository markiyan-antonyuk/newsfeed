package com.markantoni.newsfeed.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.markantoni.newsfeed.R
import com.markantoni.newsfeed.repository.model.Article
import com.markantoni.newsfeed.util.downloadImage
import com.markantoni.newsfeed.util.inflate
import com.markantoni.newsfeed.util.loadImage
import com.markantoni.newsfeed.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_article_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleDetailsFragment : BaseFragment() {
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
        articleSaveCb.setOnCheckedChangeListener { _, isChecked -> if (isChecked) saveArticle() else deleteArticle() }
        articleImage.transitionName = article.title

        viewModel.article.observe(this, Observer { bindArticle(it) })
        viewModel.error.observe(this, Observer { showErrorSnackbar { viewModel.loadArticle(article.id) } })
        viewModel.loadArticle(article.id)
    }

    private fun bindArticle(article: Article, firstBind: Boolean = false) {
        this.article = article

        articleTitle.text = article.title
        articleCategory.text = article.category
        articleSaveCb.isChecked = article.isSaved
        articleDescription.isVisible = article.description?.let {
            articleDescription.text = it
            true
        } ?: false

        articleImage.loadImage(article.image, false)
        if (firstBind && article.isSaved) requireContext().downloadImage(article.image)
    }

    private fun saveArticle() {
        requireContext().downloadImage(article.image)
        viewModel.saveArticle(article)
    }

    private fun deleteArticle() {
        viewModel.deleteArticle(article)
    }
}