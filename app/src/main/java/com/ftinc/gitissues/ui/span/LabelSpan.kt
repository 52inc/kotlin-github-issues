package com.ftinc.gitissues.ui.span

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.annotation.ColorInt
import android.support.v4.graphics.ColorUtils
import android.text.style.ReplacementSpan
import com.ftinc.gitissues.util.dpToPx
import com.ftinc.kit.util.Utils

/**
 * Created by r0adkll on 11/5/16.
 */

class LabelSpan(val context: Context, val color: Int): ReplacementSpan() {

    val radius: Float
    val outline: Paint

    init {
        radius = context.dpToPx(2f)

        outline = Paint()
        outline.color = Color.argb((.12f * 255).toInt(), 0, 0, 0)
        outline.style = Paint.Style.STROKE
        outline.strokeWidth = context.dpToPx(1f)
    }

    override fun getSize(paint: Paint?, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return Math.round(measureText(paint, text, start, end)?.toDouble()!!).toInt() + (radius * 2f).toInt()
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val nx = x+radius
        val rect = RectF(nx-radius, top.toFloat()-radius/2, (nx+radius) + measureText(paint, text, start, end)!!, bottom.toFloat() + radius/2f)
        paint.color = color
        canvas.drawRoundRect(rect, radius, radius, paint)
        canvas.drawRoundRect(rect, radius, radius, outline)
        paint.color = getTextColor()
        canvas.drawText(text, start, end, x+radius, y.toFloat(), paint)
    }

    private fun measureText(paint: Paint?, text: CharSequence?, start: Int, end: Int): Float? {
        return paint?.measureText(text, start, end) ?: 0f + (radius * 4f)
    }

    @ColorInt
    fun getTextColor(): Int{
        if(ColorUtils.calculateContrast(Color.WHITE, color) < 3.0){
            return Color.argb((.87f * 255).toInt(), 0, 0, 0)
        }else{
            return Color.WHITE
        }
    }

}