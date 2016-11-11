package com.ftinc.gitissues.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.LevelListDrawable
import android.graphics.drawable.ShapeDrawable
import android.support.v4.graphics.ColorUtils
import android.util.AttributeSet
import android.widget.TextView
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Label
import com.ftinc.gitissues.util.color
import com.ftinc.gitissues.util.colorFromHex
import com.ftinc.gitissues.util.dipToPx
import com.ftinc.gitissues.util.drawable

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.ui.widget
 * Created by drew.heavner on 11/3/16.
 */

class LabelView : TextView{

    var labelColor: Int = color(R.color.grey_500)
        get() = field
        set(value) {
            field = value
            background?.setColorFilter(value, PorterDuff.Mode.SRC_ATOP)
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        parseAttributes(attrs, defStyleAttr)
        initialize()
    }

    constructor(context: Context, label: Label) : super(context) {
        labelColor = label.color.colorFromHex()
        text = label.name

        if(ColorUtils.calculateContrast(Color.WHITE, labelColor) < 3.0){
            val color = color(R.color.black87)
            setTextColor(color)
        }else{
            setTextColor(Color.WHITE)
        }

        initialize()
    }

    private fun parseAttributes(attrs: AttributeSet?, defStyleAttr: Int){
        val a: TypedArray? = context.obtainStyledAttributes(attrs, R.styleable.LabelView, defStyleAttr, 0)
        labelColor = a?.getColor(R.styleable.LabelView_lv_color, -1) ?: color(R.color.grey_500)
        a?.recycle()
    }

    private fun initialize(){
        // Setup padding
        var padL:Int = dipToPx(8f)
        var padR:Int = dipToPx(8f)
        var padT:Int = dipToPx(4f)
        var padB:Int = dipToPx(4f)

        // Normalize padding
        padT = normalize(paddingTop, padT)
        padB = normalize(paddingBottom, padB)
        padL = normalize(paddingLeft, padL)
        padR = normalize(paddingRight, padR)
        setPadding(padL, padT, padR, padB)

        // Setup text constraints
        val bg:Drawable = drawable(R.drawable.dr_label_background)
        val bgOutline: Drawable = drawable(R.drawable.dr_label_background_outline)
        bg.setColorFilter(labelColor, PorterDuff.Mode.SRC_ATOP)

        val levelListBg: LayerDrawable = LayerDrawable(arrayOf(bg, bgOutline))
        background = levelListBg
    }

    fun normalize(value: Int, default: Int): Int = if(default > value) default else value

}
