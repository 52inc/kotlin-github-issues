package com.ftinc.gitissues.ui.screens.home.recents

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Issue
import com.ftinc.gitissues.ui.BaseFragment
import com.ftinc.kit.widget.EmptyView

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.ui.screens.home.recents
 * Created by drew.heavner on 11/3/16.
 */

class RecentsFragment : BaseFragment(), RecentsView {

    /***********************************************************************************************
     *
     * Variables
     *
     */

    val recycler: RecyclerView by bindView(R.id.recycler)
    val emptyView: EmptyView by bindView(R.id.empty_view)



    /***********************************************************************************************
     *
     * Lifecycle Methods
     *
     */

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_item_list, container, false)
    }

    /***********************************************************************************************
     *
     * Base Methods
     *
     */

    override fun setupComponent(){

    }

    /***********************************************************************************************
     *
     * View Methods
     *
     */

    override fun setRecentItems(items: List<Issue>) {

    }

    override fun setLoading(flag: Boolean) {
        when(flag){
            true -> emptyView.setLoading()
            false -> emptyView.setEmpty()
        }
    }

}
