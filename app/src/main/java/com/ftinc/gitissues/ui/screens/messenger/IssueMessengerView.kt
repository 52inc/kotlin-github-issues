package com.ftinc.gitissues.ui.screens.messenger

import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import com.ftinc.gitissues.api.Label
import com.ftinc.gitissues.api.Milestone
import com.ftinc.gitissues.api.User
import com.ftinc.gitissues.ui.BaseView
import com.ftinc.gitissues.ui.adapter.delegate.BaseIssueMessage
import com.ftinc.gitissues.ui.adapter.delegate.CommentIssueMessage

/**
 * Created by r0adkll on 11/6/16.
 */

interface IssueMessengerView: BaseView{

    fun setStatus(status: String, @ColorRes color: Int)
    fun setMessengerItems(items: List<BaseIssueMessage>)
    fun updateIssueMessengerItem(item: BaseIssueMessage)

    fun hideLoading()
    fun appendComment(comment: CommentIssueMessage?)
    fun hideInput()

    fun setEditableLabels(labels: List<Label>?, selectedMap: MutableMap<Label, Boolean>)
    fun setEditableMilestones(milestones: List<Milestone>?, currentMilestone: Milestone?)
    fun setEditableAssignees(assignees: List<User>?, selectedMap: MutableMap<User, Boolean>)

}