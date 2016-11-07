package com.ftinc.gitissues.ui.adapter.delegate

import android.app.Activity
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.TextAppearanceSpan
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
import com.ftinc.gitissues.api.toGithubDate
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
        vh.bind(item.event)
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
        val formatedText: CharSequence = when(e){
            Events.CLOSED -> if (event.commit_id.isNullOrBlank()) {
                buildSpannable(
                        EventSpan(event.actor.login, StyleSpan(Typeface.BOLD)),
                        EventSpan("closed this issue about ${event.created_at.toGithubDate()?.timeAgo()}", null))
            } else {
                buildSpannable(
                        EventSpan(event.actor.login, StyleSpan(Typeface.BOLD)),
                        EventSpan("closed this issue in", null),
                        EventSpan(event.commit_id!!.slice(0..6), URLSpan(event.commit_url)),
                        EventSpan(event.created_at.toGithubDate()?.timeAgo().toString(), null))
            }
            Events.REOPENED -> buildSpannable(EventSpan(event.actor.login, StyleSpan(Typeface.BOLD)), EventSpan("reopened this issue ${event.created_at.toGithubDate()?.timeAgo()}", null))
//            Events.SUBSCRIBED -> ""
            Events.MERGED -> buildSpannable(
                    EventSpan(event.actor.login, StyleSpan(Typeface.BOLD)),
                    EventSpan("merged commit", null),
                    EventSpan(event.commit_id!!.slice(0..6), URLSpan(event.commit_url)),
                    EventSpan(event.created_at.toGithubDate()?.timeAgo().toString(), null)
            )
            Events.REFERENCED -> buildSpannable(
                    EventSpan(event.actor.login, StyleSpan(Typeface.BOLD)),
                    EventSpan("referenced this issue from commit", null),
                    EventSpan(event.commit_id?.slice(0..6).toString(), URLSpan(event.commit_url)),
                    EventSpan(event.created_at.toGithubDate()?.timeAgo().toString(), null)
            )
//            Events.MENTIONED -> ""
            Events.ASSIGNED -> buildSpannable(
                    EventSpan(event.assignee?.login.toString(), StyleSpan(Typeface.BOLD)),
                    EventSpan("was assigned by", null),
                    EventSpan(event.assigner?.login.toString(), StyleSpan(Typeface.BOLD)),
                    EventSpan(event.created_at.toGithubDate()?.timeAgo().toString(), null)
            )
            Events.UNASSIGNED -> buildSpannable(
                    EventSpan(event.assignee?.login.toString(), StyleSpan(Typeface.BOLD)),
                    EventSpan("was unassigned by", null),
                    EventSpan(event.assigner?.login.toString(), StyleSpan(Typeface.BOLD)),
                    EventSpan(event.created_at.toGithubDate()?.timeAgo().toString(), null)
            )
            Events.LABELED -> buildSpannable(
                    EventSpan(event.actor.login.toString(), StyleSpan(Typeface.BOLD)),
                    EventSpan("added the", null),
                    EventSpan(event.label?.name.toString(), LabelSpan(event.label?.color?.colorFromHex()!!, dpToPx(2f))),
                    EventSpan("label ${event.created_at.toGithubDate()?.timeAgo()}", null)
            )
            Events.UNLABELED -> buildSpannable(
                    EventSpan(event.actor.login.toString(), StyleSpan(Typeface.BOLD)),
                    EventSpan("removed the", null),
                    EventSpan(event.label?.name.toString(), LabelSpan(event.label?.color?.colorFromHex()!!, dpToPx(2f))),
                    EventSpan("label ${event.created_at.toGithubDate()?.timeAgo()}", null)
            )
            Events.MILESTONED -> buildSpannable(
                    EventSpan(event.actor.login.toString(), StyleSpan(Typeface.BOLD)),
                    EventSpan("added this to the", null),
                    EventSpan(event.milestone?.title.toString(), StyleSpan(Typeface.BOLD)),
                    EventSpan("milestone ${event.created_at.toGithubDate()?.timeAgo()}", null)
            )
            Events.DEMILESTONED -> buildSpannable(
                    EventSpan(event.actor.login.toString(), StyleSpan(Typeface.BOLD)),
                    EventSpan("removed this from the", null),
                    EventSpan(event.milestone?.title.toString(), StyleSpan(Typeface.BOLD)),
                    EventSpan("milestone ${event.created_at.toGithubDate()?.timeAgo()}", null)
            )
            Events.RENAMED -> buildSpannable(
                    EventSpan(event.actor.login.toString(), StyleSpan(Typeface.BOLD)),
                    EventSpan("renamed this issue ${event.created_at.toGithubDate()?.timeAgo()}", null)
            )
            Events.LOCKED -> buildSpannable(
                    EventSpan(event.actor.login.toString(), StyleSpan(Typeface.BOLD)),
                    EventSpan("locked this issue ${event.created_at.toGithubDate()?.timeAgo()}", null)
            )
            Events.UNLOCKED -> buildSpannable(
                    EventSpan(event.actor.login.toString(), StyleSpan(Typeface.BOLD)),
                    EventSpan("unlocked this issue ${event.created_at.toGithubDate()?.timeAgo()}", null)
            )
            Events.HEAD_REF_DELETED -> ""
            Events.HEAD_REF_RESTORED -> ""
            else -> buildSpannable(
                    EventSpan(event.actor.login.toString(), StyleSpan(Typeface.BOLD)),
                    EventSpan("modified this issue ${event.created_at.toGithubDate()?.timeAgo()}", null)
            )
        }
        text.text = formatedText

    }

    fun buildSpannable(vararg spans: EventSpan): SpannableString{
        val string: String = spans.fold("", {
            spanText, span ->
            "$spanText${span.text} "
        })

        val s: SpannableString = SpannableString(string)
        var len: Int = 0

        spans.forEach {
            if(it.span != null){
                s.setSpan(it.span, len, len+it.text.length, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            len += it.text.length+1
        }

        return s
    }

}

class EventSpan(val text: String,
                val span: Any?)