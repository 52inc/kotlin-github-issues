package com.ftinc.gitissues.ui

import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import butterknife.bindOptionalView
import com.ftinc.gitissues.App
import com.ftinc.gitissues.R
import com.ftinc.gitissues.di.components.AppComponent

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.ui
 * Created by drew.heavner on 11/3/16.
 */
abstract class BaseActivity : AppCompatActivity(){

    /***********************************************************************************************
     *
     * Variables
     *
     */

    protected val toolbar: Toolbar? by bindOptionalView(R.id.appbar)

    /***********************************************************************************************
     *
     * Lifecycle Methods
     *
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupComponent(App.appComponent)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setSupportActionBar(toolbar)
    }

    /***********************************************************************************************
     *
     * Helper Methods
     *
     */

    private fun getContentView(): View = findViewById(android.R.id.content)

    /***********************************************************************************************
     *
     * Base Methods
     *
     */

    /**
     * Easily get the color for a given resource Id
     */
    @ColorInt
    protected fun color(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)

    fun showSnackBar(msg: String) {
        showSnackBar(msg, Snackbar.LENGTH_SHORT)
    }

    fun showSnackBar(@StringRes resId: Int) {
        showSnackBar(resId, Snackbar.LENGTH_SHORT)
    }

    fun showSnackBar(t: Throwable) {
        showSnackBar(t, Snackbar.LENGTH_SHORT)
    }

    fun showSnackBar(message: String, @Snackbar.Duration duration: Int) {
        Snackbar.make(getContentView(), message, duration).show()
    }

    fun showSnackBar(@StringRes resId: Int, @Snackbar.Duration duration: Int) {
        Snackbar.make(getContentView(), resId, duration).show()
    }

    fun showSnackBar(t: Throwable, @Snackbar.Duration duration: Int) {
        Snackbar.make(getContentView(), t.message.toString(), duration).show()
    }

    /**
     * Setup you DI component here
     */
    protected abstract fun setupComponent(component: AppComponent)

}