package com.ftinc.gitissues.util

import android.content.res.ColorStateList
import android.support.annotation.ColorInt

/**
 * Project: ClearChatAndroid
 * Package: com.clearchat.app.presentation.util
 * Created by drew.heavner on 9/8/16.
 */

object ColorUtils {

    fun colorToStateList(@ColorInt color: Int): ColorStateList {
        return ColorStateList(arrayOf(intArrayOf()), intArrayOf(color))
    }

}
