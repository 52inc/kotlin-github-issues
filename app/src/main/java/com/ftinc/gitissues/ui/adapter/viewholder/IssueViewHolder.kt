package com.ftinc.gitissues.ui.adapter.viewholder

import `in`.uncod.android.bypass.Bypass
import android.support.v7.widget.RecyclerView
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
import com.ftinc.gitissues.api.toGithubDate
import com.ftinc.gitissues.ui.widget.LabelView
import com.ftinc.gitissues.util.ImageSpanTarget
import com.ftinc.gitissues.util.color
import com.ftinc.gitissues.util.timeAgo
import com.ftinc.kit.widget.BezelImageView
import timber.log.Timber

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.ui.screens.home.recents.adapter
 * Created by drew.heavner on 11/3/16.
 */

class IssueViewHolder(view: View) : RecyclerView.ViewHolder(view){

    val number: TextView by bindView(R.id.number)
    val title: TextView by bindView(R.id.title)
    val body: TextView by bindView(R.id.body)
    val assigneeAvatar: BezelImageView by bindView(R.id.assignee_avatar)
    val assigneeName: TextView by bindView(R.id.assignee_name)
    val updated: TextView by bindView(R.id.updated)
    val status: LabelView by bindView(R.id.status)
    val commentCount: TextView by bindView(R.id.comment_count)

    companion object{
        fun create(inflater: LayoutInflater, parent: ViewGroup?): IssueViewHolder {
            return IssueViewHolder(inflater.inflate(R.layout.item_issue, parent, false))
        }
    }

    fun bind(issue: Issue, bypass: Bypass){

        number.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, if(issue.pull_request != null) R.drawable.ic_source_pull else 0)

        number.text = "#${issue.number}"
        title.text = issue.title
        assigneeName.text = issue.assignee?.login
        updated.text = issue.updated_at?.toGithubDate()?.timeAgo()

        status.text = issue.state.toUpperCase()
        val clr: Int = if(issue.state.equals("open", true)) {
            itemView.color(R.color.green_500)
        }else{
            itemView.color(R.color.red_500)
        }
        status.labelColor = clr

        commentCount.text = issue.comments.toString()

        body.text = bypass.markdownToSpannable(issue.body, body, { s, imageLoadingSpan ->
            Timber.i("Load markdown image: $s")
            Glide.with(itemView.context)
                    .load(s)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ImageSpanTarget(body, imageLoadingSpan))
        })

        // Load avatar from url
        Glide.with(itemView.context)
                .load(issue.assignee?.avatar_url)
                .placeholder(R.drawable.dr_avatar_default)
                .crossFade()
                .into(assigneeAvatar)
    }

}

