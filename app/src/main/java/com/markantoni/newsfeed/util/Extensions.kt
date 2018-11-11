package com.markantoni.newsfeed.util

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false) =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun Context.getColorCompat(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun ImageView.loadImage(url: String?, crossFade: Boolean = true) {
    Glide
        .with(context)
        .load(url)
        .apply(RequestOptions().centerCrop()).apply {
            if (crossFade) transition(DrawableTransitionOptions.withCrossFade())
        }
        .into(this)
}

fun Context.downloadImage(url: String) {
    Glide
        .with(this)
        .downloadOnly()
        .load(url)
        .submit()
}

fun String.toTimestamp(): Long {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return format.parse(this).time
}