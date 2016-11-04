package com.ftinc.gitissues.ui.screens.home.recents.adapter

import `in`.uncod.android.bypass.Bypass
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
(activity: Activity) : BetterRecyclerAdapter<Issue, IssueViewHolder>() {

    private var inflater: LayoutInflater
    private val bypass: Bypass = Bypass(activity, Bypass.Options())

    init {
        inflater = activity.layoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): IssueViewHolder {
        return IssueViewHolder.create(inflater, parent)
    }

    override fun onBindViewHolder(vh: IssueViewHolder?, i: Int) {
        super.onBindViewHolder(vh, i)
        vh?.bind(getItem(i), bypass)
    }

}