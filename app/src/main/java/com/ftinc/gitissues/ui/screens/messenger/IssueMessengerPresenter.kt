package com.ftinc.gitissues.ui.screens.messenger

import com.ftinc.gitissues.api.Label
import com.ftinc.gitissues.api.Milestone
import com.ftinc.gitissues.api.User

/**
 * Created by r0adkll on 11/6/16.
 */

interface IssueMessengerPresenter{
    fun loadIssueContent()
    fun createComment(markdown: String)
    fun updateLabels(labels: List<Label>)
    fun updateMilestone(milestone: Milestone?)
    fun updateAssignees(currentUser: List<User>?)
}