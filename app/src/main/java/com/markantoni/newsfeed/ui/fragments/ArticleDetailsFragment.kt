package com.markantoni.newsfeed.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.markantoni.newsfeed.R
import com.markantoni.newsfeed.repository.model.Article
import com.markantoni.newsfeed.util.downloadImage
import com.markantoni.newsfeed.util.inflate
import com.markantoni.newsfeed.util.loadImage
import com.markantoni.newsfeed.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_article_details.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ArticleDetailsFragment : BaseFragment() {
    companion object {
        private const val KEY_ARTICLE = "key.article"
        private const val KEY_TRANSITION_NAME = "key.transition_name"
        fun newInstance(article: Article, transitionName: String) = ArticleDetailsFragment().apply {
            arguments = bundleOf(KEY_ARTICLE to article, KEY_TRANSITION_NAME to transitionName)
        }
    }

    private val viewModel by sharedViewModel<ArticleViewModel>()
    private lateinit var article: Article

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        container?.inflate(R.layout.fragment_article_details)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindArticle(arguments?.getParcelable(KEY_ARTICLE) ?: error("Must pass an article"), true)
        articleImage.transitionName = arguments?.getString(KEY_TRANSITION_NAME)

        viewModel.article.observe(this, Observer { bindArticle(it) })
        viewModel.error.observe(this, Observer { showErrorSnackbar { viewModel.loadArticle(article.id) } })
        viewModel.loadArticle(article.id)
    }

    private fun bindArticle(article: Article, firstBind: Boolean = false) {
        this.article = article

        articleTitle.text = article.title
        articleCategory.text = article.category
        articleDescription.text = article.description
        articleImage.loadImage(article.image, false)

        articleSaveCb.apply {
            setOnCheckedChangeListener(null)
            isChecked = article.isSaved
            setOnCheckedChangeListener { _, isChecked -> if (isChecked) saveArticle() else deleteArticle() }
        }

        articlePinCb.apply {
            setOnCheckedChangeListener(null)
            isChecked = viewModel.isPinned(article)
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) viewModel.pinArticle(article) else viewModel.unpinArticle(article)
            }
        }

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