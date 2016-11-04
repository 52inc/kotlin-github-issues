package com.ftinc.gitissues.ui.screens.home.repositories

import com.ftinc.gitissues.api.GithubAPI
import com.ftinc.gitissues.di.ActivityScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by r0adkll on 11/3/16.
 */
@Module
class ReposModule(val view: ReposView) {

    @Provides @ActivityScope
    fun provideView(): ReposView = view

    @Provides @ActivityScope
    fun providePresenter(api: GithubAPI, view: ReposView): ReposPresenter = ReposPresenterImpl(api, view)

}