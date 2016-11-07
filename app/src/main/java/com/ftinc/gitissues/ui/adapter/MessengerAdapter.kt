package com.ftinc.gitissues.ui.adapter

import `in`.uncod.android.bypass.Bypass
import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftinc.gitissues.ui.adapter.delegate.BaseIssueMessage
import com.ftinc.gitissues.ui.adapter.delegate.IssueCommentDelegate
import com.ftinc.gitissues.ui.adapter.delegate.IssueEventDelegate
import com.ftinc.gitissues.ui.adapter.delegate.IssueMessageDelegate
import com.ftinc.kit.adapter.BetterRecyclerAdapter
import com.hannesdorfmann.adapterdelegates2.AdapterDelegatesManager

/**
 * Created by r0adkll on 11/5/16.
 */

class MessengerAdapter

(activity: Activity) : ListRecyclerAdapter<BaseIssueMessage, RecyclerView.ViewHolder>(activity) {

    private val delegateManager: AdapterDelegatesManager<List<BaseIssueMessage>>
    private val bypass: Bypass = Bypass(activity, Bypass.Options())

    init {
        delegateManager = AdapterDelegatesManager()

        delegateManager.addDelegate(IssueMessageDelegate(activity, bypass))
        delegateManager.addDelegate(IssueCommentDelegate(activity, bypass))
        delegateManager.addDelegate(IssueEventDelegate(activity))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return delegateManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, i: Int) {
        delegateManager.onBindViewHolder(items, i, vh)
    }

    override fun getItemViewType(position: Int): Int {
        return delegateManager.getItemViewType(items, position)
    }

    override fun getItemId(position: Int): Long {
        if(position > 0) {
            return items[position].getId()
        }
        return RecyclerView.NO_ID
    }
}