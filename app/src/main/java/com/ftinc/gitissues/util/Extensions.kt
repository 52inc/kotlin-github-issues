package com.ftinc.gitissues.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ftinc.kit.util.TimeUtils
import com.ftinc.kit.util.Utils
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.util
 * Created by drew.heavner on 11/3/16.
 */


fun View.dpToPx(dp: Float) : Float = Utils.dpToPx(this.context, dp)
fun View.dipToPx(dp: Float) : Int = Utils.dipToPx(this.context, dp)@ColorInt fun View.color(@ColorRes resId: Int) : Int = ContextCompat.getColor(this.context, resId)
fun View.drawable(@DrawableRes resId: Int) : Drawable = ContextCompat.getDrawable(this.context, resId)
fun View.visible() { this.visibility = View.VISIBLE }
fun View.invisible(){ this.visibility = View.INVISIBLE }
fun View.gone(){ this.visibility = View.GONE }

fun Context.dpToPx(dp: Float) : Float = Utils.dpToPx(this, dp)
fun Context.dipToPx(dp: Float) : Int = Utils.dipToPx(this, dp)

fun RecyclerView.ViewHolder.dpToPx(dp: Float) : Float = Utils.dpToPx(this.itemView.context, dp)
fun RecyclerView.ViewHolder.dipToPx(dp: Float) : Int = Utils.dipToPx(this.itemView.context, dp)
@ColorInt fun RecyclerView.ViewHolder.color(@ColorRes resId: Int) : Int = ContextCompat.getColor(this.itemView.context, resId)
fun RecyclerView.ViewHolder.string(@StringRes resId: Int, vararg formatArgs: Any): String = this.itemView.context.getString(resId, formatArgs)


fun View.setVisible(visible: Boolean) {
    this.visibility = if(visible) View.VISIBLE else View.GONE
}
fun View.setVisibleWeak(visible: Boolean) {
    this.visibility = if(visible) View.VISIBLE else View.INVISIBLE
}

fun Date.timeAgo(): String? = TimeUtils.getTimeAgo(this.time)
fun Date.longFormat(): String = SimpleDateFormat("MMMM d, yyyy", Locale.US).format(this)
fun String.colorFromHex(): Int {
    val hex: String = if(this.startsWith("#")) this else "#$this"
    return Color.parseColor(hex)
}