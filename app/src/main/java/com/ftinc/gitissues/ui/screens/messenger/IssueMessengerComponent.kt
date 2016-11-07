package com.ftinc.gitissues.ui.screens.messenger

import com.ftinc.gitissues.di.ActivityScope
import dagger.Subcomponent

/**
 * Created by r0adkll on 11/6/16.
 */

@ActivityScope
@Subcomponent(modules = arrayOf(
        IssueMessengerModule::class
))
interface IssueMessengerComponent{
    fun inject(activity: IssueMessengerActivity)
}