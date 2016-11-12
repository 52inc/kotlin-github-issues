package com.ftinc.gitissues.ui.span

import android.text.SpannableString

/**
 * Created by r0adkll on 11/12/16.
 */

class EventSpan(val text: String,
                val span: Any?)

class EventSpanned{

    val items = arrayListOf<EventSpan>()

    fun span(text: String, span: Any?) = items.add(EventSpan(text, span))
    fun span(text: String) = items.add(EventSpan(text, null))

    fun build(): SpannableString {
        val string: String = items.fold("", {
            spanText, span ->
            "$spanText${span.text} "
        })

        val s: SpannableString = SpannableString(string)
        var len: Int = 0

        items.forEach {
            if(it.span != null){
                s.setSpan(it.span, len, len+it.text.length, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            len += it.text.length+1
        }

        return s
    }

}

fun fancyText(init: EventSpanned.() -> Unit): EventSpanned {
    val text = EventSpanned()
    text.init()
    return text
}
