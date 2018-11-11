package com.markantoni.newsfeed.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.markantoni.newsfeed.R
import com.markantoni.newsfeed.repository.model.Article
import com.markantoni.newsfeed.ui.adapters.ArticlesAdapter
import com.markantoni.newsfeed.ui.adapters.PinnedArticlesAdapter
import com.markantoni.newsfeed.util.inflate
import com.markantoni.newsfeed.viewmodel.ArticleViewModel
import com.markantoni.newsfeed.viewmodel.FeedViewModel
import com.markantoni.newsfeed.viewmodel.NavigationViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {
    private val viewModel by viewModel<FeedViewModel>()
    private val navigationViewModel by sharedViewModel<NavigationViewModel>()
    private val articleViewModel by sharedViewModel<ArticleViewModel>()

    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var pinnedAdapter: PinnedArticlesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        container?.inflate(R.layout.fragment_home)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val articleClicked: (Article, ImageView) -> Unit = { article, image ->
            navigationViewModel.requestShowArticleDetails(article, image)
        }
        articlesAdapter = ArticlesAdapter(articleClicked)
        pinnedAdapter = PinnedArticlesAdapter(articleClicked)

        flattenCb.setOnCheckedChangeListener { _, isChecked -> flattenArticles(isChecked) }
        homeSwipeToRefresh.setOnRefreshListener { viewModel.reloadArticles() }
        homeArticlesRv.adapter = articlesAdapter
        homeArticlesRv.layoutManager = GridLayoutManager(requireContext(), 1)
        homePinnedRv.adapter = pinnedAdapter
        homePinnedRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.articles.observe(this, Observer { articlesAdapter.submitList(it) })
        viewModel.articlesLoading.observe(this, Observer { homeSwipeToRefresh.isRefreshing = it })
        viewModel.initialArticle.observe(this, Observer(viewModel::scheduleCheckNewArticles))
        viewModel.articlesError.observe(this, Observer {
            homeSwipeToRefresh.isRefreshing = false
            showErrorSnackbar { viewModel.reloadArticles() }
        })
        viewModel.articlesAvailable.observe(this, Observer {
            viewModel.cancelScheduledCheckNewArticles()
            showSnackbar(R.string.message_articles_available, R.string.label_load) { viewModel.reloadArticles() }
        })
        articleViewModel.pinnedArticles.observe(this, Observer {
            pinnedAdapter.submitList(it)
        })
    }

    private fun flattenArticles(flatten: Boolean) {
        articlesAdapter.isFlattened = flatten
        TransitionManager.beginDelayedTransition(homeArticlesRv)
        articlesAdapter.notifyItemRangeChanged(0, articlesAdapter.itemCount - 1)
        (homeArticlesRv.layoutManager as GridLayoutManager).spanCount = if (flatten) 3 else 1
    }
}