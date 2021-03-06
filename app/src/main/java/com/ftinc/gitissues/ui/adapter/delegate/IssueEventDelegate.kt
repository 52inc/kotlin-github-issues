package com.ftinc.gitissues.ui.adapter.delegate

import android.app.Activity
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Event
import com.ftinc.gitissues.api.Events
import com.ftinc.gitissues.api.githubTimeAgo
import com.ftinc.gitissues.ui.span.EventSpanned
import com.ftinc.gitissues.ui.span.LabelSpan
import com.ftinc.gitissues.ui.span.fancyText
import com.ftinc.gitissues.util.*
import com.hannesdorfmann.adapterdelegates2.AdapterDelegate

/**
 * Created by r0adkll on 11/5/16.
 */

class IssueEventDelegate(val activity: Activity): AdapterDelegate<List<BaseIssueMessage>>{

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        return EventViewHolder.create(activity.layoutInflater, parent)
    }

    override fun onBindViewHolder(items: List<BaseIssueMessage>, position: Int, holder: RecyclerView.ViewHolder) {
        val vh: EventViewHolder = holder as EventViewHolder
        val item: EventIssueMessage = items[position] as EventIssueMessage
        vh.bind(item)
    }

    override fun isForViewType(items: List<BaseIssueMessage>, position: Int): Boolean {
        return items[position] is EventIssueMessage
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

    fun bind(eventMessage: EventIssueMessage){
        val event = eventMessage.getEvent()
        val e: Events = Events.find(event.event)

        icon.setImageResource(when(e){
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
        })

        icon.imageTintList = ColorUtils.colorToStateList(color(when(e){
            Events.CLOSED -> R.color.red_400
            Events.REOPENED -> R.color.green_400
            Events.MERGED -> R.color.purple_800
            else -> R.color.grey_400
        }))

        // Generate text
        val formatedText: EventSpanned = when(e){
            Events.CLOSED -> fancyText {
                if (event.commit_id.isNullOrBlank()) {
                    span(event.actor.login, StyleSpan(Typeface.BOLD))
                    span("closed this issue about ${event.created_at.githubTimeAgo()}")
                }else {
                    span(event.actor.login, StyleSpan(Typeface.BOLD))
                    span("closed this issue in")
                    span(event.commit_id!!.slice(0..6), URLSpan(event.commit_url))
                    span(event.created_at.githubTimeAgo())
                }
            }
            Events.REOPENED -> fancyText {
                span(event.actor.login, StyleSpan(Typeface.BOLD))
                span("reopened this issue ${event.created_at.githubTimeAgo()}")
            }
            Events.MERGED -> fancyText {
                span(event.actor.login, StyleSpan(Typeface.BOLD))
                span("merged commit")
                span(event.commit_id!!.slice(0..6), URLSpan(event.commit_url))
                span(event.created_at.githubTimeAgo())
            }
            Events.REFERENCED -> fancyText {
                span(event.actor.login, StyleSpan(Typeface.BOLD))
                span("referenced this issue from commit")
                span(event.commit_id?.slice(0..6).toString(), URLSpan(event.commit_url))
                span(event.created_at.githubTimeAgo())
            }
            Events.ASSIGNED -> fancyText {
                if(event.assignee?.id != event.assigner?.id) {
                    span(event.assignee?.login.toString(), StyleSpan(Typeface.BOLD))
                    span("was assigned by")
                    span(event.assigner?.login.toString(), StyleSpan(Typeface.BOLD))
                    span(event.created_at.githubTimeAgo())
                }else{
                    span(event.assignee?.login.toString(), StyleSpan(Typeface.BOLD))
                    span("self-assigned this")
                    span(event.created_at.githubTimeAgo())
                }
            }
            Events.UNASSIGNED -> fancyText {
                if (event.assignee?.id != event.assigner?.id) {
                    span(event.assignee?.login.toString(), StyleSpan(Typeface.BOLD))
                    span("was unassigned by")
                    span(event.assigner?.login.toString(), StyleSpan(Typeface.BOLD))
                    span(event.created_at.githubTimeAgo())
                } else {
                    span(event.assignee?.login.toString(), StyleSpan(Typeface.BOLD))
                    span("removed their assignment")
                    span(event.created_at.githubTimeAgo())
                }
            }
            Events.LABELED -> fancyText {
                span(event.actor.login.toString(), StyleSpan(Typeface.BOLD))
                if(eventMessage.events.size == 1) {
                    span("added the")
                    span(event.label?.name.toString(), LabelSpan(itemView.context, event.label?.color?.colorFromHex()!!))
                    span("label ${event.created_at.githubTimeAgo()}")
                }else{
                    span("added")
                    eventMessage.events.forEach {
                        span(it.label?.name.toString(), LabelSpan(itemView.context, it.label?.color?.colorFromHex()!!))
                    }
                    span("labels ${event.created_at.githubTimeAgo()}")
                }
            }
            Events.UNLABELED -> fancyText {
                span(event.actor.login.toString(), StyleSpan(Typeface.BOLD))
                span("removed the")
                span(event.label?.name.toString(), LabelSpan(itemView.context, event.label?.color?.colorFromHex()!!))
                span("label ${event.created_at.githubTimeAgo()}")
            }
            Events.MILESTONED -> fancyText {
                span(event.actor.login.toString(), StyleSpan(Typeface.BOLD))
                span("added this to the")
                span(event.milestone?.title.toString(), StyleSpan(Typeface.BOLD))
                span("milestone ${event.created_at.githubTimeAgo()}")
            }
            Events.DEMILESTONED -> fancyText {
                span(event.actor.login.toString(), StyleSpan(Typeface.BOLD))
                span("removed this from the")
                span(event.milestone?.title.toString(), StyleSpan(Typeface.BOLD))
                span("milestone ${event.created_at.githubTimeAgo()}")
            }
            Events.RENAMED -> fancyText {
                span(event.actor.login.toString(), StyleSpan(Typeface.BOLD))
                span("renamed this issue from")
                span("${event.rename?.from}", StyleSpan(Typeface.BOLD))
                span("to")
                span("${event.rename?.to}", StyleSpan(Typeface.BOLD))
                span(event.created_at.githubTimeAgo())
            }
            Events.LOCKED -> fancyText {
                span(event.actor.login.toString(), StyleSpan(Typeface.BOLD))
                span("locked this issue ${event.created_at.githubTimeAgo()}")
            }
            Events.UNLOCKED -> fancyText {
                span(event.actor.login.toString(), StyleSpan(Typeface.BOLD))
                span("unlocked this issue ${event.created_at.githubTimeAgo()}")
            }
//            Events.HEAD_REF_DELETED -> ""
//            Events.HEAD_REF_RESTORED -> ""
            else -> fancyText {
                span("[${e.name}]")
                span(event.actor.login.toString(), StyleSpan(Typeface.BOLD))
                span("modified this issue ${event.created_at.githubTimeAgo()}")
            }
        }

        text.text = formatedText.build()

    }

}