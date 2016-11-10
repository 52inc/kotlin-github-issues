package com.ftinc.gitissues.util

import android.graphics.Rect
import android.widget.Toast
import android.util.DisplayMetrics
import android.view.View
import android.view.Gravity
import android.view.Window


/**
 * Created by r0adkll on 11/9/16.
 */

object Tools{

    fun positionToast(toast: Toast, view: View, offsetX: Int, offsetY: Int) {
        // toasts are positioned relatively to decor view, views relatively to their parents, we have to gather additional data to have a common coordinate system
        val rect = Rect()
        view.rootView.getWindowVisibleDisplayFrame(rect)

        // covert anchor view absolute position to a position which is relative to decor view
        val viewLocation = IntArray(2)
        view.getLocationInWindow(viewLocation)
        val viewLeft = viewLocation[0] - rect.left
        val viewTop = viewLocation[1] - rect.top

        // measure toast to center it relatively to the anchor view
        val metrics = view.context.resources.displayMetrics
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(metrics.widthPixels, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(metrics.heightPixels, View.MeasureSpec.UNSPECIFIED)
        toast.view.measure(widthMeasureSpec, heightMeasureSpec)
        val toastWidth = toast.view.measuredWidth

        // compute toast offsets
        val toastX = viewLeft + (view.getWidth() - toastWidth) / 2 + offsetX
        val toastY = viewTop + view.getHeight() + offsetY
        toast.setGravity(Gravity.LEFT or Gravity.TOP, toastX, toastY)
    }

    fun visibility(visible: Boolean, vararg views: View){
        views.forEach { it.visibility = if(visible) View.VISIBLE else View.GONE }
    }

    fun visibilityWeak(visible: Boolean, vararg views: View){
        views.forEach { it.visibility = if(visible) View.VISIBLE else View.INVISIBLE }
    }

}

