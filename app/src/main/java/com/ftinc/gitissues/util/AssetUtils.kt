package com.ftinc.gitissues.util

import android.content.Context
import java.io.BufferedInputStream
import java.io.InputStream

/**
 * Created by r0adkll on 11/7/16.
 */
object AssetUtils{

    fun getAssetString(context: Context, filename: String): String{
        val input = context.assets.open(filename)
        val bytes: ByteArray = input.readBytes()
        return String(bytes)
    }

}

fun Context.assetString(filename: String): String = AssetUtils.getAssetString(this, filename)