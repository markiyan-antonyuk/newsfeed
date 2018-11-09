package com.markantoni.newsfeed.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class CoroutineViewModel : ViewModel(), CoroutineScope {
    val error = MutableLiveData<Throwable>()
    private val job = Job()
    override val coroutineContext =
        Dispatchers.Main + job + CoroutineExceptionHandler { _, throwable ->
            Log.e("NewsfeedApp", "", throwable)
            error.value = throwable
        }

    override fun onCleared() = job.cancel()
}