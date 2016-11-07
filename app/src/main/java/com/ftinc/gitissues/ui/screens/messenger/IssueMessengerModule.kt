package com.ftinc.gitissues.ui.screens.messenger

import com.ftinc.gitissues.api.GithubAPI
import com.ftinc.gitissues.api.Issue
import com.ftinc.gitissues.di.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Created by r0adkll on 11/6/16.
 */
@Module
class IssueMessengerModule(val issue: Issue, val view: IssueMessengerView){

    @Provides @ActivityScope
    fun provideIssue(): Issue = issue

    @Provides @ActivityScope
    fun provideView(): IssueMessengerView = view

    @Provides @ActivityScope
    fun providePresenter(issue: Issue,
                         api: GithubAPI,
                         view: IssueMessengerView): IssueMessengerPresenter{
        return IssueMessengerPresenterImpl(issue, api, view)
    }

}