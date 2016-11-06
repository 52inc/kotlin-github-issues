package com.ftinc.gitissues.ui.screens.home.pullrequests

import com.ftinc.gitissues.api.GithubAPI
import com.ftinc.gitissues.di.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Created by r0adkll on 11/4/16.
 */
@Module
class PullRequestsModule(val view: PullRequestsView){

    @Provides @ActivityScope
    fun provideView(): PullRequestsView = view

    @Provides @ActivityScope
    fun providePresenter(api: GithubAPI, view: PullRequestsView) : PullRequestsPresenter = PullRequestsPresenterImpl(api, view)

}