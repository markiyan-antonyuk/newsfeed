package com.markantoni.newsfeed.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.markantoni.newsfeed.R
import com.markantoni.newsfeed.extensions.inflate
import com.markantoni.newsfeed.extensions.loadImage
import com.markantoni.newsfeed.repository.model.Article
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
        articleSaveBtn.setOnClickListener { saveArticle() }
        articleImage.transitionName = article.title

        viewModel.article.observe(this, Observer { bindArticle(it) })
        viewModel.error.observe(this, Observer { showErrorSnackbar { viewModel.loadArticle(article.id) } })
        viewModel.loadArticle(article.id)
    }

    override fun onStop() {
        dismissSnackbar()
        super.onStop()
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

    private fun saveArticle() {
        Glide
            .with(requireContext())
            .downloadOnly()
            .load(article.image)
            .submit()
        viewModel.saveArticle(article)
    }
}