package com.markantoni.newsfeed.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.markantoni.newsfeed.R
import java.text.SimpleDateFormat
import java.util.*

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false) =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun Context.getColorCompat(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun View.showErrorSnackbar(retryAction: () -> Unit) {
    Snackbar
        .make(this, R.string.error_general, Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.label_retry) { retryAction() }
        .show()
}

fun ImageView.loadImage(url: String?, crossFade: Boolean = true) {
    Glide
        .with(context)
        .load(url)
        .apply(RequestOptions().centerCrop()).apply {
            if (crossFade) transition(DrawableTransitionOptions.withCrossFade())
        }
        .into(this)
}

fun String.toTimestamp(): Long {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return format.parse(this).time
}