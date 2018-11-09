package com.markantoni.newsfeed.datasource

import org.koin.dsl.module.module

val dataSourceModule = module {
    factory { ArticlesDataSourceFactory() }
}