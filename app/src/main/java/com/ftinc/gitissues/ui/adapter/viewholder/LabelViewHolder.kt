package com.ftinc.gitissues.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import butterknife.bindView
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Label
import com.ftinc.gitissues.ui.widget.LabelView
import com.ftinc.gitissues.util.colorFromHex

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.ui.adapter.viewholder
 * Created by drew.heavner on 11/10/16.
 */

class LabelViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    val name: LabelView by bindView(R.id.name)
    val checked: CheckBox by bindView(R.id.checked)

    companion object{
        fun create(inflater: LayoutInflater, parent: ViewGroup): LabelViewHolder{
            return LabelViewHolder(inflater.inflate(R.layout.item_label, parent, false))
        }
    }

    fun bind(label: Label,
             isChecked: Boolean,
             clickListener: View.OnClickListener){
        name.text = label.name
        name.labelColor = label.color.colorFromHex()
        name.setOnClickListener(clickListener)
        checked.isChecked = isChecked
    }

}