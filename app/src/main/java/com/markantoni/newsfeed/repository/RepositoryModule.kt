package com.markantoni.newsfeed.repository

import com.markantoni.newsfeed.repository.db.DatabaseRepository
import com.markantoni.newsfeed.repository.network.NetworkRepository
import org.koin.dsl.module.module

val repositoryModule = module {
    single { NewsFeedRepository() } bind Repository::class
    single { NetworkRepository() }
    single { DatabaseRepository() }
}