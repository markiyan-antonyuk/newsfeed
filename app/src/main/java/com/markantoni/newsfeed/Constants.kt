package com.markantoni.newsfeed

import java.util.concurrent.TimeUnit

object Constants {
    val BACKGROUND_CHECK_INTERVAL = TimeUnit.MINUTES.toMillis(15)
    val FOREGROUND_CHECK_INTERVAL = TimeUnit.SECONDS.toMillis(30)

    const val INITIAL_PAGE_SIZE = 25
    const val PAGE_SIZE = 10
}