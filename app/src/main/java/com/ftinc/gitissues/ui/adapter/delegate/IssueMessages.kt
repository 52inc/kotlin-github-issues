package com.ftinc.gitissues.ui.adapter.delegate

import com.ftinc.gitissues.api.Comment
import com.ftinc.gitissues.api.Event
import com.ftinc.gitissues.api.Issue
import com.ftinc.gitissues.api.toGithubDate
import java.util.*

/**
 * Created by r0adkll on 11/5/16.
 */

interface BaseIssueMessage {
    fun getId(): Long
    fun getCreatedDate(): Long
}

class IssueMessage(val issue: Issue) : BaseIssueMessage {
    override fun getCreatedDate(): Long {
        return issue.created_at.toGithubDate()?.time ?: 0
    }

    override fun getId(): Long {
        return issue.id
    }

    override fun hashCode(): Int {
        return issue.hashCode()
    }
}

class CommentIssueMessage(val comment: Comment) : BaseIssueMessage {
    override fun getCreatedDate(): Long {
        return comment.created_at.toGithubDate()?.time ?: 0
    }

    override fun getId(): Long {
        return comment.id
    }

    override fun hashCode(): Int {
        return comment.hashCode()
    }
}

class EventIssueMessage(val events: List<Event>) : BaseIssueMessage {

    constructor(event: Event) : this(listOf(event))

    fun getEvent(): Event = events[0]

    override fun getCreatedDate(): Long {
        return getEvent().created_at.toGithubDate()?.time ?: 0
    }

    override fun getId(): Long {
        return getEvent().id
    }

    override fun hashCode(): Int {
        return getEvent().hashCode()
    }

}