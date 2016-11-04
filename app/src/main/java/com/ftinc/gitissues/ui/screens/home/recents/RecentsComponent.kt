package com.ftinc.gitissues.ui.screens.home.recents

import com.ftinc.gitissues.di.ActivityScope
import dagger.Subcomponent

/**
 * Created by r0adkll on 11/3/16.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(
        RecentsModule::class
))
interface RecentsComponent {
    fun inject(fragment: RecentsFragment)
}