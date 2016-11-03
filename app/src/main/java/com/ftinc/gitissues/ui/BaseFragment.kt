package com.ftinc.gitissues.ui

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import com.ftinc.gitissues.di.components.HasComponent

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.ui
 * Created by drew.heavner on 11/3/16.
 */
abstract class BaseFragment : Fragment() {

    private lateinit var fragView: View

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupComponent()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragView = view!!
    }

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
        Snackbar.make(fragView, message, duration).show()
    }

    fun showSnackBar(@StringRes resId: Int, @Snackbar.Duration duration: Int) {
        Snackbar.make(fragView, resId, duration).show()
    }

    fun showSnackBar(t: Throwable, @Snackbar.Duration duration: Int) {
        Snackbar.make(fragView, t.message.toString(), duration)
    }

    /**
     * Override as a place to setup this fragment's component graph
     */
    protected open fun setupComponent() {}

    /**
     * Gets a component for dependency injection by its type.
     */
    protected fun <C> getComponent(componentType: Class<C>): C {
        return componentType.cast((activity as HasComponent<*>).component)
    }

}