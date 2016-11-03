package com.ftinc.gitissues.ui.screens.home.recents

import com.ftinc.gitissues.api.Issue
import com.ftinc.gitissues.ui.BaseView

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.ui.screens.home.recents
 * Created by drew.heavner on 11/3/16.
 */
interface RecentsView : BaseView{
    fun setRecentItems(items: List<Issue>)
    fun setLoading(flag: Boolean)
}
