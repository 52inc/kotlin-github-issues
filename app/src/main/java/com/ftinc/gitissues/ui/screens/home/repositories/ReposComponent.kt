package com.ftinc.gitissues.ui.screens.home.repositories

import com.ftinc.gitissues.di.ActivityScope
import dagger.Subcomponent

/**
 * Created by r0adkll on 11/3/16.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(
        ReposModule::class
))
interface ReposComponent{
    fun inject(fragment: ReposFragment)
}