package com.markantoni.newsfeed.ui.fragments

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.markantoni.newsfeed.R

abstract class BaseFragment : Fragment() {
    private var snackbar: Snackbar? = null

    protected fun showSnackbar(@StringRes messageRes: Int, @StringRes actionRes: Int, action: () -> Unit) {
        view?.let {
            snackbar = Snackbar
                .make(it, messageRes, Snackbar.LENGTH_INDEFINITE)
                .setAction(actionRes) { action() }
                .apply { show() }
        }
    }

    protected fun showErrorSnackbar(action: () -> Unit) =
        showSnackbar(R.string.error_general, R.string.label_retry, action)

    protected fun dismissSnackbar() {
        snackbar?.dismiss()
    }
}