package com.ftinc.gitissues.ui.screens.home.repositories

import com.ftinc.gitissues.api.GithubAPI
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by r0adkll on 11/3/16.
 */

class ReposPresenterImpl(val api: GithubAPI, val view: ReposView): ReposPresenter{

    override fun loadUserRepos() {
        view.setLoading(true)
        api.getRepositories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ repos ->
                    view.setLoading(false)
                    view.setRepoItems(repos)
                }, { error ->
                    view.setLoading(false)
                    view.showSnackBar(error)
                })
    }

}