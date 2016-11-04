package com.ftinc.gitissues.ui.screens.home

import com.ftinc.gitissues.di.ActivityScope
import com.ftinc.gitissues.di.modules.ActivityModule
import com.ftinc.gitissues.ui.screens.home.recents.RecentsComponent
import com.ftinc.gitissues.ui.screens.home.recents.RecentsModule
import com.ftinc.gitissues.ui.screens.home.repositories.ReposComponent
import com.ftinc.gitissues.ui.screens.home.repositories.ReposModule
import dagger.Subcomponent

/**
 * Created by r0adkll on 11/3/16.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(
        ActivityModule::class
))
interface HomeComponent{
    fun plus(module: RecentsModule): RecentsComponent
    fun plus(module: ReposModule): ReposComponent
}
