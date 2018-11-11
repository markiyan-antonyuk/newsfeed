package com.markantoni.newsfeed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import androidx.transition.TransitionInflater
import com.markantoni.newsfeed.service.NewsFeedService
import com.markantoni.newsfeed.ui.fragments.ArticleDetailsFragment
import com.markantoni.newsfeed.ui.fragments.HomeFragment
import com.markantoni.newsfeed.viewmodel.NavigationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val navigationViewModel by viewModel<NavigationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.findFragmentById(R.id.fragmentContainer) ?: run {
            supportFragmentManager.transaction {
                replace(R.id.fragmentContainer, HomeFragment())
            }
        }

        navigationViewModel.requestedShowArticleDetails.observe(this, Observer { navigateToArticleDetails(it) })
    }

    private fun navigateToArticleDetails(transition: NavigationViewModel.ArticleTransition) {
        supportFragmentManager.transaction {
            val fragment = ArticleDetailsFragment.newInstance(transition.article, transition.image.transitionName).apply {
                    val sharedTransition = TransitionInflater.from(this@MainActivity).inflateTransition(R.transition.image)
                    sharedElementEnterTransition = sharedTransition
                    sharedElementReturnTransition = sharedTransition
                }

            transition.image.let { addSharedElement(it, it.transitionName) }
            setCustomAnimations(android.R.anim.fade_in, 0, 0, android.R.anim.fade_out)
            replace(R.id.fragmentContainer, fragment)
            addToBackStack(null)
        }
    }

    override fun onStart() {
        super.onStart()
        stopService(NewsFeedService.newIntent(this))
    }

    override fun onStop() {
        startService(NewsFeedService.newIntent(this))
        super.onStop()
    }
}