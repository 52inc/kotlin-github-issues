package com.ftinc.gitissues.ui.screens.home.recents.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftinc.gitissues.api.Issue
import com.ftinc.kit.adapter.BetterRecyclerAdapter
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager

/**
 * The Recents page delegate adapter
 */
class RecentsAdapter

/**
 * Constructor
 */
(activity: Activity) : BetterRecyclerAdapter<Issue, RecyclerView.ViewHolder>() {

    protected var inflater: LayoutInflater

    init {
        inflater = activity.layoutInflater

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return LatestIssueViewHolder.create(inflater, parent)
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder?, i: Int) {
        super.onBindViewHolder(vh, i)

    }

//    override fun getItemViewType(position: Int): Int {
//        return delegateManager.getItemViewType(getItems(), position)
//    }
}