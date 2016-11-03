package com.ftinc.gitissues.ui

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.ui
 * Created by drew.heavner on 11/3/16.
 */
interface BaseView {
    fun showSnackBar(message: String)
    fun showSnackBar(@StringRes resId: Int)
    fun showSnackBar(t: Throwable)
    fun showSnackBar(message: String, @Snackbar.Duration duration: Int)
    fun showSnackBar(@StringRes resId: Int, @Snackbar.Duration duration: Int)
    fun showSnackBar(t: Throwable, @Snackbar.Duration duration: Int)
}
