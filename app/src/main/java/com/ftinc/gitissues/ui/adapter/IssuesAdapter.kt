package com.ftinc.gitissues.ui.adapter

import `in`.uncod.android.bypass.Bypass
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftinc.gitissues.api.Issue
import com.ftinc.gitissues.ui.adapter.viewholder.IssueViewHolder
import com.ftinc.kit.adapter.BetterRecyclerAdapter

/**
 * The Recents page delegate adapter
 */
class IssuesAdapter

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