package com.markantoni.newsfeed.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ArticleModel::class), version = 1)
abstract class ArticlesDatabase : RoomDatabase() {
    abstract fun articlesDao(): ArticlesDao
}