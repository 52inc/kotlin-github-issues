package com.ftinc.gitissues.ui.adapter.delegate

import `in`.uncod.android.bypass.Bypass
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.bumptech.glide.Glide
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Comment
import com.ftinc.gitissues.api.toGithubDate
import com.ftinc.gitissues.util.timeAgo
import com.ftinc.kit.widget.BezelImageView
import com.hannesdorfmann.adapterdelegates2.AdapterDelegate

/**
 * Created by r0adkll on 11/5/16.
 */

class IssueCommentDelegate(val inflater: LayoutInflater, val bypass: Bypass) : AdapterDelegate<List<BaseIssueMessage>>{

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        return CommentViewHolder.create(inflater, parent)
    }

    override fun isForViewType(items: List<BaseIssueMessage>, position: Int): Boolean {
        return items.get(position) is CommentIssueMessage
    }

    override fun onBindViewHolder(items: List<BaseIssueMessage>, position: Int, holder: RecyclerView.ViewHolder) {
        val vh: CommentViewHolder = holder as CommentViewHolder
        val item: CommentIssueMessage = items.get(position) as CommentIssueMessage
        vh.bind(item.comment, bypass)
    }

}

class CommentViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    val avatar: BezelImageView by bindView(R.id.owner_avatar)
    val name: TextView by bindView(R.id.owner_name)
    val editedDate: TextView by bindView(R.id.edited_date)
    val commentDate: TextView by bindView(R.id.comment_date)
    val body: TextView by bindView(R.id.body)

    companion object{
        fun create(inflater: LayoutInflater, parent: ViewGroup?) : CommentViewHolder{
            return CommentViewHolder(inflater.inflate(R.layout.item_message_comment, parent, false))
        }
    }

    fun bind(comment: Comment, bypass: Bypass){

        name.text = comment.user.login
        commentDate.text = "commented ${comment.created_at.toGithubDate()?.timeAgo()}"

        if(comment.updated_at != null){
            editedDate.visibility = View.VISIBLE
            editedDate.text = "edited ${comment.updated_at.toGithubDate()?.timeAgo()}"
        }else{
            editedDate.visibility = View.GONE
        }

        body.text = bypass.markdownToSpannable(comment.body, body, null)

        Glide.with(itemView.context)
                .load(comment.user.avatar_url)
                .placeholder(R.drawable.dr_avatar_default)
                .crossFade()
                .into(avatar)

    }

}