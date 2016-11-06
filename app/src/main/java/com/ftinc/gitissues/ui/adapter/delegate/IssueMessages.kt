package com.ftinc.gitissues.ui.adapter.delegate

import com.ftinc.gitissues.api.Comment
import com.ftinc.gitissues.api.Event
import com.ftinc.gitissues.api.Issue

/**
 * Created by r0adkll on 11/5/16.
 */

interface BaseIssueMessage {
    fun getId(): Long
}

class IssueMessage(val issue: Issue) : BaseIssueMessage {
    override fun getId(): Long {
        return issue.id
    }
}

class CommentIssueMessage(val comment: Comment) : BaseIssueMessage {
    override fun getId(): Long {
        return comment.id
    }
}

class EventIssueMessage(val event: Event) : BaseIssueMessage {
    override fun getId(): Long {
        return event.id
    }
}