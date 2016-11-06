package com.ftinc.gitissues.ui.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftinc.gitissues.ui.adapter.delegate.BaseIssueMessage
import com.ftinc.kit.adapter.BetterRecyclerAdapter
import com.hannesdorfmann.adapterdelegates2.AdapterDelegatesManager

/**
 * Created by r0adkll on 11/5/16.
 */

class MessengerAdapter

(activity: Activity) : BetterRecyclerAdapter<BaseIssueMessage, RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater
    private val delegateManager: AdapterDelegatesManager<List<BaseIssueMessage>>

    init {
        inflater = activity.layoutInflater
        delegateManager = AdapterDelegatesManager()



    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder?, i: Int) {
        super.onBindViewHolder(vh, i)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

}