package com.ftinc.gitissues.ui.screens.home.repositories

import com.ftinc.gitissues.api.Repository
import com.ftinc.gitissues.ui.BaseView

/**
 * Created by r0adkll on 11/3/16.
 */
interface ReposView : BaseView{
    fun setRepoItems(items: List<Repository>)
    fun setLoading(flag: Boolean)
}
