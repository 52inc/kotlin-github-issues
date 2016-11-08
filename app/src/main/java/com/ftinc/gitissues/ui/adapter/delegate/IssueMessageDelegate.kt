package com.ftinc.gitissues.ui.adapter.delegate

import `in`.uncod.android.bypass.Bypass
import android.app.Activity
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Issue
import com.ftinc.gitissues.util.ImageSpanTarget
import com.ftinc.gitissues.util.assetString
import com.hannesdorfmann.adapterdelegates2.AdapterDelegate
import timber.log.Timber

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

        vh.bind(activity, item.issue, bypass)
    }

}

class IssueMessageViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    val text: TextView by bindView(R.id.text)

    companion object{
        fun create(inflater: LayoutInflater, parent: ViewGroup?): IssueMessageViewHolder{
            return IssueMessageViewHolder(inflater.inflate(R.layout.item_message_issue, parent, false))
        }
    }

    fun bind(activity: Activity, issue: Issue, bypass: Bypass){
        text.text = bypass.markdownToSpannable(issue.body, text, { s, imageLoadingSpan ->
            Timber.i("Load markdown image: $s")
            Glide.with(activity)
                    .load(s)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ImageSpanTarget(text, imageLoadingSpan))
        })
        text.movementMethod = LinkMovementMethod.getInstance()
    }

}