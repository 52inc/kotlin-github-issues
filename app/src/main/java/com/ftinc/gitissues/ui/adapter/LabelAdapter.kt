package com.ftinc.gitissues.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ftinc.gitissues.api.Label
import com.ftinc.gitissues.ui.adapter.viewholder.LabelViewHolder
import com.ftinc.kit.adapter.BetterRecyclerAdapter

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.ui.adapter
 * Created by drew.heavner on 11/10/16.
 */

class LabelAdapter(context: Context) : BetterRecyclerAdapter<Label, LabelViewHolder>(){

    val checkedState = mutableMapOf<Label, Boolean>()
    val selectedLabels: List<Label>
        get() {
            return checkedState.filterValues { it }.keys.toList()
        }

    var inflater: LayoutInflater

    init{
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LabelViewHolder {
        return LabelViewHolder.create(inflater, parent!!)
    }

    override fun onBindViewHolder(vh: LabelViewHolder, i: Int) {
        val label = items[i]
        vh.bind(label, checkedState.getOrElse(label, { ->
            false
        }), View.OnClickListener {
            toggleState(label, i)
        })
    }

    override fun getFilter(): Filter<Label> {
        return Filter { item, query ->
            if(!query.isNullOrEmpty()){
                item.name.contains(query.toString(), ignoreCase = true)
            }
            true
        }
    }

    override fun clear() {
        super.clear()
        checkedState.clear()
    }

    fun toggleState(label: Label, i: Int){
        val state = checkedState.getOrElse(label, { ->
            false
        })
        checkedState.put(label, !state)
        this.notifyItemChanged(i)
    }

}
