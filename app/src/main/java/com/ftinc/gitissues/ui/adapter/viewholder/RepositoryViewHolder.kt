package com.ftinc.gitissues.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.bumptech.glide.Glide
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Repository
import com.ftinc.gitissues.api.toGithubDate
import com.ftinc.gitissues.ui.widget.LabelView
import com.ftinc.gitissues.util.setVisible
import com.ftinc.gitissues.util.timeAgo
import com.ftinc.kit.widget.BezelImageView

/**
 * Created by r0adkll on 11/4/16.
 */

class RepositoryViewHolder(view: View) : RecyclerView.ViewHolder(view){

    val title: TextView by bindView(R.id.title)
    val description: TextView by bindView(R.id.description)
    val forks: TextView by bindView(R.id.forks)
    val ownerAvatar: BezelImageView by bindView(R.id.owner_avatar)
    val ownerName: TextView by bindView(R.id.owner_name)
    val updated: TextView by bindView(R.id.updated)
    val stars: TextView by bindView(R.id.stars)
    val issues: TextView by bindView(R.id.issues)

    companion object{
        fun create(inflater: LayoutInflater, parent: ViewGroup?): RepositoryViewHolder {
            return RepositoryViewHolder(inflater.inflate(R.layout.item_repository, parent, false))
        }
    }

    fun bind(repo: Repository){
        title.text = repo.full_name
        description.text = repo.description
        forks.text = repo.forks_count.toString()
        ownerName.text = repo.owner.login
        updated.text = repo.updated_at.toGithubDate()?.timeAgo()
        stars.text = repo.stargazers_count.toString()
        issues.text = repo.open_issues_count.toString()
        issues.setVisible(repo.has_issues)

        title.setCompoundDrawablesRelativeWithIntrinsicBounds(if(repo.private) R.drawable.ic_lock_black_24dp else 0, 0, 0, 0)

        Glide.with(itemView.context)
                .load(repo.owner.avatar_url)
                .placeholder(R.drawable.dr_avatar_default)
                .crossFade()
                .into(ownerAvatar)
    }

}