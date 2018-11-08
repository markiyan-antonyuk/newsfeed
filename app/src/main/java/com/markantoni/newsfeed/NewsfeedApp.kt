package com.markantoni.newsfeed

import android.app.Application
import com.markantoni.newsfeed.repository.network.networkModule
import com.markantoni.newsfeed.repository.repositoryModule
import com.markantoni.newsfeed.viewmodel.viewModelModule
import org.koin.android.ext.android.startKoin

class NewsfeedApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(repositoryModule, networkModule, viewModelModule))
    }
}