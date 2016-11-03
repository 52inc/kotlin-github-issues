package com.ftinc.gitissues.util

import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.View
import com.ftinc.kit.util.Utils

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.util
 * Created by drew.heavner on 11/3/16.
 */


fun View.dpToPx(dp: Float) : Float = Utils.dpToPx(this.context, dp)
fun View.dipToPx(dp: Float) : Int = Utils.dipToPx(this.context, dp)
@ColorInt fun View.color(@ColorRes resId: Int) : Int = ContextCompat.getColor(this.context, resId)
fun View.drawable(@DrawableRes resId: Int) : Drawable = ContextCompat.getDrawable(this.context, resId)