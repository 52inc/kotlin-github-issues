package com.ftinc.gitissues.ui.screens.home.pullrequests

import com.ftinc.gitissues.api.GithubAPI
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by r0adkll on 11/4/16.
 */

class PullRequestsPresenterImpl(val api: GithubAPI, val view: PullRequestsView) : PullRequestsPresenter{

    override fun loadPullRequests() {
        api.searchPullRequests()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ searchResults ->
                    view.setLoading(false)
                    view.setPullRequestsItems(searchResults.items)
                }, { error ->
                    view.setLoading(false)
                    view.showSnackBar(error)
                })
    }

}