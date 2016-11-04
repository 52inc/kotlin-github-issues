package com.ftinc.gitissues.di.components

import com.ftinc.gitissues.MainActivity
import com.ftinc.gitissues.di.modules.ActivityModule
import com.ftinc.gitissues.di.modules.ApiModule
import com.ftinc.gitissues.di.modules.AppModule
import com.ftinc.gitissues.ui.screens.home.HomeComponent
import dagger.Component
import javax.inject.Singleton

/**
 * Created by r0adkll on 11/2/16.
 */
@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        ApiModule::class
))
interface AppComponent{
    fun inject(activity: MainActivity)
    fun plus(module: ActivityModule): HomeComponent
}