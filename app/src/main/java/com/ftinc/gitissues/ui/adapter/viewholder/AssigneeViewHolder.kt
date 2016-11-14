package com.ftinc.gitissues.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.bumptech.glide.Glide
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.User
import com.ftinc.gitissues.util.color
import com.ftinc.kit.widget.BezelImageView

/**
 * Created by r0adkll on 11/13/16.
 */

class AssigneeViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    val avatar: BezelImageView by bindView(R.id.avatar)
    val login: TextView by bindView(R.id.login)
    val name: TextView by bindView(R.id.name)
    val checked: ImageView by bindView(R.id.checked)

    companion object{
        fun create(inflater: LayoutInflater, parent: ViewGroup?): AssigneeViewHolder{
            return AssigneeViewHolder(inflater.inflate(R.layout.item_assignee, parent, false))
        }
    }

    fun bind(user: User, isCurrent: Boolean){
        login.text = user.login
        name.text = user.name
        checked.visibility = if(isCurrent) View.VISIBLE else View.GONE

        itemView.setBackgroundColor(if(isCurrent) color(R.color.grey_200) else 0)

        // load image
        Glide.with(itemView.context)
                .load(user.avatar_url)
                .crossFade()
                .into(avatar)
    }

}