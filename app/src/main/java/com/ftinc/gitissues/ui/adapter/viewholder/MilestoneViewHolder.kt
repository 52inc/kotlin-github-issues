package com.ftinc.gitissues.ui.adapter.viewholder

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.bindView
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Milestone
import com.ftinc.gitissues.api.githubTimeAgo
import com.ftinc.gitissues.api.toGithubDate
import com.ftinc.gitissues.ui.span.fancyText
import com.ftinc.gitissues.util.longFormat
import com.ftinc.gitissues.util.string

/**
 * Created by r0adkll on 11/12/16.
 */

class MilestoneViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){

    val title: TextView by bindView(R.id.title)
    val dueDate: TextView by bindView(R.id.due_date)
    val updated: TextView by bindView(R.id.updated)
    val description: TextView by bindView(R.id.description)
    val progress: ProgressBar by bindView(R.id.progress)
    val progressComplete: TextView by bindView(R.id.progress_complete_label)
    val progressOpen: TextView by bindView(R.id.progress_open_label)
    val progressClosed: TextView by bindView(R.id.progress_closed_label)

    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup?): MilestoneViewHolder{
            return MilestoneViewHolder(inflater.inflate(R.layout.item_milestone, parent, false))
        }
    }

    fun bind(milestone: Milestone, isCurrentMilestone: Boolean){
        title.text = milestone.title
        title.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, if(isCurrentMilestone) R.drawable.ic_check_circle_green_500_18dp else 0, 0)

        updated.text = "Last updated ${milestone.updated_at.githubTimeAgo()}"

        if(milestone.due_on != null){
            dueDate.text = "Due by ${milestone.due_on.toGithubDate()?.longFormat()}"
            dueDate.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_calendar_clock, 0, 0, 0)
        }else{
            dueDate.text = string(R.string.label_milestone_no_dueon)
            dueDate.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        }

        description.text = milestone.description

        val percent = (milestone.closed_issues.toFloat() / (milestone.closed_issues + milestone.open_issues).toFloat() * 100).toInt()
        progress.progress = percent
        progress.isIndeterminate = false
        progressComplete.text = fancyText {
            span("$percent%", StyleSpan(Typeface.BOLD))
            span(string(R.string.complete))
        }.build()

        progressOpen.text = fancyText {
            span("${milestone.open_issues}", StyleSpan(Typeface.BOLD))
            span(string(R.string.open))
        }.build()

        progressClosed.text = fancyText {
            span("${milestone.closed_issues}", StyleSpan(Typeface.BOLD))
            span(string(R.string.closed))
        }.build()

    }

}