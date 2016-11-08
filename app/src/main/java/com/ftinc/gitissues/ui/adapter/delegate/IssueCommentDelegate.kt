package com.ftinc.gitissues.ui.adapter.delegate

import `in`.uncod.android.bypass.Bypass
import android.app.Activity
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Comment
import com.ftinc.gitissues.api.githubTimeAgo
import com.ftinc.gitissues.api.toGithubDate
import com.ftinc.gitissues.util.ImageSpanTarget
import com.ftinc.gitissues.util.timeAgo
import com.ftinc.kit.widget.BezelImageView
import com.hannesdorfmann.adapterdelegates2.AdapterDelegate
import timber.log.Timber

/**
 * Created by r0adkll on 11/5/16.
 */

class IssueCommentDelegate(val activity: Activity, val bypass: Bypass) : AdapterDelegate<List<BaseIssueMessage>>{

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        return CommentViewHolder.create(activity.layoutInflater, parent)
    }

    override fun isForViewType(items: List<BaseIssueMessage>, position: Int): Boolean {
        return items.get(position) is CommentIssueMessage
    }

    override fun onBindViewHolder(items: List<BaseIssueMessage>, position: Int, holder: RecyclerView.ViewHolder) {
        val vh: CommentViewHolder = holder as CommentViewHolder
        val item: CommentIssueMessage = items.get(position) as CommentIssueMessage
        vh.bind(activity, item.comment, bypass)
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

    fun bind(activity: Activity, comment: Comment, bypass: Bypass){

        name.text = comment.user.login
        commentDate.text = "commented ${comment.created_at.githubTimeAgo()}"

        if(comment.updated_at != null && comment.updated_at != comment.created_at){
            editedDate.visibility = View.VISIBLE
            editedDate.text = "edited ${comment.updated_at.githubTimeAgo()}"
        }else{
            editedDate.visibility = View.GONE
        }

        body.text = bypass.markdownToSpannable(comment.body, body, { s, imageLoadingSpan ->
            Timber.i("Load markdown image: $s")
            Glide.with(activity)
                    .load(s)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ImageSpanTarget(body, imageLoadingSpan))
        })
        body.movementMethod = LinkMovementMethod.getInstance()

        Glide.with(itemView.context)
                .load(comment.user.avatar_url)
                .placeholder(R.drawable.dr_avatar_default)
                .crossFade()
                .into(avatar)

    }

}