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

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false) =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun Context.getColorCompat(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun View.showErrorSnackbar(retryAction: () -> Unit) {
    Snackbar
        .make(this, R.string.error_general, Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.label_retry) { retryAction() }
        .show()
}

fun ImageView.loadImage(url: String?) {
    Glide
        .with(context)
        .load(url)
        .apply(RequestOptions().centerCrop())
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}