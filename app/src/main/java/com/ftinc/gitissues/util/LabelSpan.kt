package com.ftinc.gitissues.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan

/**
 * Created by r0adkll on 11/5/16.
 */

class LabelSpan(val color: Int): ReplacementSpan() {



    \\
    override fun getSize(paint: Paint?, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return Math.round(measureText(paint, text, start, end));
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val rect = RectF(x, top.toFloat(), x + measureText(paint, text, start, end), bottom.toFloat())
        paint.color = Color.BLUE
        canvas.drawRoundRect(rect, 100f, 30f, paint)
        paint.color = Color.WHITE
        canvas.drawText(text, start, end, x, y.toFloat(), paint)
    }

    private fun measureText(paint: Paint?, text: CharSequence?, start: Int, end: Int): Float {
        return paint.measureText(text, start, end)
    }

}