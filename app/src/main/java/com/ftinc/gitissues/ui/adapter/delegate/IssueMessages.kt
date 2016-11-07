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
}

class CommentIssueMessage(val comment: Comment) : BaseIssueMessage {
    override fun getCreatedDate(): Long {
        return comment.created_at.toGithubDate()?.time ?: 0
    }

    override fun getId(): Long {
        return comment.id
    }
}

class EventIssueMessage(val event: Event) : BaseIssueMessage {
    override fun getCreatedDate(): Long {
        return event.created_at.toGithubDate()?.time ?: 0
    }

    override fun getId(): Long {
        return event.id
    }
}