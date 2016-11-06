package com.ftinc.gitissues.ui.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Event
import com.ftinc.gitissues.api.Events
import com.hannesdorfmann.adapterdelegates2.AdapterDelegate

/**
 * Created by r0adkll on 11/5/16.
 */

class IssueEventDelegate(val inflater: LayoutInflater): AdapterDelegate<List<BaseIssueMessage>>{

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(items: List<BaseIssueMessage>, position: Int, holder: RecyclerView.ViewHolder) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isForViewType(items: List<BaseIssueMessage>, position: Int): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

class EventViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    val icon: ImageView by bindView(R.id.icon)
    val text: TextView by bindView(R.id.text)

    companion object{
        fun create(inflater: LayoutInflater, parent: ViewGroup?): EventViewHolder{
            return EventViewHolder(inflater.inflate(R.layout.item_message_event, parent, false))
        }
    }

    fun bind(event: Event){
        val e: Events = Events.find(event.event)
        val resId: Int = when(e){
            Events.LABELED -> R.drawable.ic_tag
            Events.UNLABELED -> R.drawable.ic_tag
            Events.ASSIGNED -> R.drawable.ic_person_black_24dp
            Events.UNASSIGNED -> R.drawable.ic_person_black_24dp
            Events.CLOSED -> R.drawable.ic_block_black_24dp
            Events.REOPENED -> R.drawable.ic_open_in_new_black_24dp
            Events.MILESTONED -> R.drawable.ic_seal
            Events.DEMILESTONED -> R.drawable.ic_seal
            Events.MERGED -> R.drawable.ic_source_merge
            Events.REFERENCED -> R.drawable.ic_bookmark_black_24dp
            Events.HEAD_REF_DELETED -> R.drawable.ic_source_branch
            Events.HEAD_REF_RESTORED -> R.drawable.ic_source_branch
            else -> R.drawable.ic_mode_edit_black_24dp
        }
        icon.setImageResource(resId)

        // Generate text


    }

}