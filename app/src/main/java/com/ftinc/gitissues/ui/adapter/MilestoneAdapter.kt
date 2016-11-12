package com.ftinc.gitissues.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftinc.gitissues.api.Milestone
import com.ftinc.gitissues.ui.adapter.viewholder.MilestoneViewHolder
import com.ftinc.kit.adapter.BetterRecyclerAdapter

/**
 * Created by r0adkll on 11/12/16.
 */

class MilestoneAdapter

(activity: Activity) : BetterRecyclerAdapter<Milestone, MilestoneViewHolder>() {

    private val inflater: LayoutInflater
    var currentMilestone: Milestone? = null

    init{
        inflater = activity.layoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MilestoneViewHolder {
        return MilestoneViewHolder.create(inflater, parent)
    }

    override fun onBindViewHolder(vh: MilestoneViewHolder?, i: Int) {
        val item = items[i]
        vh?.bind(item, isCurrentMilestone(item))

        vh?.itemView?.setOnClickListener {
            currentMilestone = item
            notifyDataSetChanged()
        }
    }

    fun isCurrentMilestone(milestone: Milestone): Boolean{
        return if(currentMilestone != null) currentMilestone?.equals(milestone) ?: false else false
    }

}