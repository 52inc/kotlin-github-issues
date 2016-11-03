package com.ftinc.gitissues.ui.screens.home.recents.adapter

import android.app.Activity
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Issue
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.ui.screens.home.recents.adapter
 * Created by drew.heavner on 11/3/16.
 */
//class LatestIssueDelegate : AdapterDelegate<List<Issue>>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
//        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun onBindViewHolder(items: List<Issue>, position: Int, holder: RecyclerView.ViewHolder, payloads: MutableList<Any>?) {
//        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun isForViewType(items: List<Issue>, position: Int): Boolean {
//        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//}

class LatestIssueViewHolder(view: View) : RecyclerView.ViewHolder(view){



    companion object{
        fun create(inflater: LayoutInflater, parent: ViewGroup?): LatestIssueViewHolder {
            return LatestIssueViewHolder(inflater.inflate(R.layout.item_issue_latest, parent, false))
        }
    }

    fun bind(issue: Issue){

    }

}

