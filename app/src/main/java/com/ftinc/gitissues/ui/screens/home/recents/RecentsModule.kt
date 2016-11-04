package com.ftinc.gitissues.ui.screens.home.recents

import com.ftinc.gitissues.api.GithubAPI
import com.ftinc.gitissues.di.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Created by r0adkll on 11/3/16.
 */

@Module
class RecentsModule(val view: RecentsView) {

    @Provides @ActivityScope
    fun provideView(): RecentsView = view

    @Provides @ActivityScope
    fun providePresenter(api: GithubAPI, view: RecentsView): RecentsPresenter = RecentsPresenterImpl(api, view)

}