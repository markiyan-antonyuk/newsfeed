package com.markantoni.newsfeed.repository.db

import androidx.room.Room
import org.koin.dsl.module.module

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), ArticlesDatabase::class.java, "database").build()
    } bind ArticlesDatabase::class
}