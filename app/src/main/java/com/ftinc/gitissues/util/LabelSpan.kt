package com.ftinc.gitissues.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan
import com.ftinc.kit.util.Utils

/**
 * Created by r0adkll on 11/5/16.
 */

class LabelSpan(val color: Int, val radius: Float): ReplacementSpan() {

    override fun getSize(paint: Paint?, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return Math.round(measureText(paint, text, start, end)?.toDouble()!!).toInt()
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val rect = RectF(x-radius, top.toFloat()-radius/2, (x+radius) + measureText(paint, text, start, end)!!, bottom.toFloat() + radius/2f)
        paint.color = color
        canvas.drawRoundRect(rect, radius, radius, paint)
        paint.color = Color.WHITE
        canvas.drawText(text, start, end, x, y.toFloat(), paint)
    }

    private fun measureText(paint: Paint?, text: CharSequence?, start: Int, end: Int): Float? {
        return paint?.measureText(text, start, end) ?: 0f + (radius * 4f)
    }

}