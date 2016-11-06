package com.ftinc.gitissues.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftinc.gitissues.api.Repository
import com.ftinc.gitissues.ui.adapter.viewholder.RepositoryViewHolder
import com.ftinc.kit.adapter.BetterRecyclerAdapter

/**
 * Created by r0adkll on 11/3/16.
 */

class ReposAdapter

(activity: Activity) : BetterRecyclerAdapter<Repository, RepositoryViewHolder>() {

    private var inflater: LayoutInflater

    init {
        inflater = activity.layoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RepositoryViewHolder {
        return RepositoryViewHolder.create(inflater, parent)
    }

    override fun onBindViewHolder(vh: RepositoryViewHolder?, i: Int) {
        super.onBindViewHolder(vh, i)
        vh?.bind(getItem(i))
    }

}