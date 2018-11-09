package com.markantoni.newsfeed.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.markantoni.newsfeed.R
import com.markantoni.newsfeed.extensions.inflate
import com.markantoni.newsfeed.extensions.showErrorSnackbar
import com.markantoni.newsfeed.ui.adapters.ArticlesAdapter
import com.markantoni.newsfeed.viewmodel.NavigationViewModel
import com.markantoni.newsfeed.viewmodel.NewsFeedViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private val viewModel by viewModel<NewsFeedViewModel>()
    private val navigationViewModel by sharedViewModel<NavigationViewModel>()
    private val articlesAdapter = ArticlesAdapter { article, imageView ->
        navigationViewModel.requestShowArticleDetails(article, imageView)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        container?.inflate(R.layout.fragment_home)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeSwipeToRefresh.setOnRefreshListener { viewModel.reloadArticles() }
        homeRecyclerView.adapter = articlesAdapter

        viewModel.articles.observe(this, Observer { articlesAdapter.submitList(it) })
        viewModel.articlesLoading.observe(this, Observer { homeSwipeToRefresh.isRefreshing = it })
        viewModel.articlesError.observe(this, Observer {
            homeSwipeToRefresh.isRefreshing = false
            view?.showErrorSnackbar { viewModel.reloadArticles() }
        })
    }
}