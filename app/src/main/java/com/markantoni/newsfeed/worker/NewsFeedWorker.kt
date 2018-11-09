//package com.markantoni.newsfeed.worker
//
//import android.content.Context
//import androidx.work.*
//import com.markantoni.newsfeed.repository.network.NetworkRepository
//import org.koin.standalone.KoinComponent
//import org.koin.standalone.inject
//
//class NewsFeedWorker(context: Context, private val params: WorkerParameters) : Worker(context, params), KoinComponent {
//    private val networkRepository by inject<NetworkRepository>()
//
//    override fun doWork(): Result {
//        networkRepository.loadArticle()
//        outputData = Data.Builder().build()
//        return Result.SUCCESS
//    }
//}