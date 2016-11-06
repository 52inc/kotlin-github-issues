package com.ftinc.gitissues.ui.screens.home.pullrequests

import com.ftinc.gitissues.di.ActivityScope
import dagger.Subcomponent

/**
 * Created by r0adkll on 11/4/16.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(
        PullRequestsModule::class
))
interface PullRequestsComponent{
    fun inject(fragment: PullRequestsFragment)
}