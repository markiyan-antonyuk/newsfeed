package com.markantoni.newsfeed

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.markantoni.newsfeed.viewmodel.ArticlesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel<ArticlesViewModel>().value.articles.observe(this, Observer {
            Log.d("mylog", "$it")
        })
    }
}