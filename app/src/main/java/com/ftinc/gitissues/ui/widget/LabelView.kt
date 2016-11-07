package com.ftinc.gitissues.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
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
        initialize()
    }

    private fun parseAttributes(attrs: AttributeSet?, defStyleAttr: Int){
        val a: TypedArray? = context.obtainStyledAttributes(attrs, R.styleable.LabelView, defStyleAttr, 0)
        labelColor = a?.getColor(R.styleable.LabelView_lv_color, -1) ?: color(R.color.grey_500)
        a?.recycle()
    }

    private fun initialize(){
        // Setup padding
        val pad:Int = dipToPx(8f)
        val padTB:Int = dipToPx(4f)
        setPadding(pad, padTB, pad, padTB)

        // Setup text constraints
        val bg:Drawable = drawable(R.drawable.dr_label_background)
        bg.setColorFilter(labelColor, PorterDuff.Mode.SRC_ATOP)
        background = bg

        setTextColor(Color.WHITE)
    }

}
