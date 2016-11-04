package com.ftinc.gitissues.ui.screens.home.recents

import com.ftinc.gitissues.api.GithubAPI
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by r0adkll on 11/3/16.
 */
class RecentsPresenterImpl(val api: GithubAPI, val view: RecentsView) : RecentsPresenter{

    override fun loadLatestIssues() {
        view.setLoading(true)
        api.getAllIssues(null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ issues ->
                    view.setLoading(false)
                    view.setRecentItems(issues)
                }, { error ->
                    view.setLoading(false)
                    view.showSnackBar(error)
                })
    }

}