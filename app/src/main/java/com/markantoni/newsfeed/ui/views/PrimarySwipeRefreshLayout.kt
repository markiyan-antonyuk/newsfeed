package com.markantoni.newsfeed.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.markantoni.newsfeed.R
import com.markantoni.newsfeed.extensions.getColorCompat

class PrimarySwipeRefreshLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    SwipeRefreshLayout(context, attrs) {

    init {
        setColorSchemeColors(context.getColorCompat(R.color.primary))
    }
}