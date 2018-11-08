package com.markantoni.newsfeed.viewmodel

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { ArticlesViewModel() }
}