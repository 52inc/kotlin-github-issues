package com.ftinc.gitissues.ui.screens.home.pullrequests

import com.ftinc.gitissues.api.Issue
import com.ftinc.gitissues.api.PullRequest
import com.ftinc.gitissues.ui.BaseView

/**
 * Created by r0adkll on 11/4/16.
 */

interface PullRequestsView : BaseView{
    fun setPullRequestsItems(items: List<Issue>)
    fun setLoading(loading: Boolean)
}