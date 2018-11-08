package com.markantoni.newsfeed.repository

import com.markantoni.newsfeed.repository.datasource.ArticlesDataSourceFactory
import com.markantoni.newsfeed.repository.network.NetworkRepository
import org.koin.dsl.module.module

val repositoryModule = module {
    factory { ArticlesDataSourceFactory() }
    single { ArticlesRepository() } bind Repository::class
    single { NetworkRepository() }
}