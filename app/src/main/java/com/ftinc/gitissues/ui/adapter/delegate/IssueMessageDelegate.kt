package com.ftinc.gitissues.ui.adapter.delegate

import `in`.uncod.android.bypass.Bypass
import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Issue
import com.hannesdorfmann.adapterdelegates2.AdapterDelegate

/**
 * Created by r0adkll on 11/6/16.
 */
class IssueMessageDelegate(val activity: Activity, val bypass: Bypass): AdapterDelegate<List<BaseIssueMessage>>{

    override fun isForViewType(items: List<BaseIssueMessage>, position: Int): Boolean {
        return items[position] is IssueMessage
    }

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        return IssueMessageViewHolder.create(activity.layoutInflater, parent)
    }

    override fun onBindViewHolder(items: List<BaseIssueMessage>, position: Int, holder: RecyclerView.ViewHolder) {
        val vh: IssueMessageViewHolder = holder as IssueMessageViewHolder
        val item: IssueMessage = items[position] as IssueMessage
        vh.bind(item.issue, bypass)
    }

}

class IssueMessageViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    val text: TextView by bindView(R.id.text)

    companion object{
        fun create(inflater: LayoutInflater, parent: ViewGroup?): IssueMessageViewHolder{
            return IssueMessageViewHolder(inflater.inflate(R.layout.item_message_issue, parent, false))
        }
    }

    fun bind(issue: Issue, bypass: Bypass){
        text.text = bypass.markdownToSpannable(issue.body, text, null)
    }

}