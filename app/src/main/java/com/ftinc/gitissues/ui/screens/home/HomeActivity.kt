package com.ftinc.gitissues.ui.screens.home

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import butterknife.bindView
import com.ftinc.gitissues.R
import com.ftinc.gitissues.di.components.AppComponent
import com.ftinc.gitissues.di.components.HasComponent
import com.ftinc.gitissues.di.modules.ActivityModule
import com.ftinc.gitissues.ui.BaseActivity
import com.ftinc.gitissues.ui.screens.home.pullrequests.PullRequestsFragment
import com.ftinc.gitissues.ui.screens.home.recents.RecentsFragment
import com.ftinc.gitissues.ui.screens.home.repositories.ReposFragment

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.ui.screens.home
 * Created by drew.heavner on 11/3/16.
 */
class HomeActivity: BaseActivity(), HasComponent<HomeComponent>{

    /***********************************************************************************************
     *
     * Variables
     *
     */

    val tabs: TabLayout by bindView(R.id.tabs)
    val pager: ViewPager by bindView(R.id.pager)
    val fab: FloatingActionButton by bindView(R.id.fab)

    lateinit var adapter: HomePagerAdapter
    lateinit var comp: HomeComponent

    /***********************************************************************************************
     *
     * Lifecycle Methods
     *
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        adapter = HomePagerAdapter(supportFragmentManager)
        pager.adapter = adapter
        tabs.setupWithViewPager(pager)
        pager.offscreenPageLimit = 2

        fab.setOnClickListener {
            // TODO: Implement circular expose of options similar to plaid
            showSnackBar("Filter Clicked!")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_home, menu)
        return true
    }

    /***********************************************************************************************
     *
     * Base Methods
     *
     */

    override fun setupComponent(component: AppComponent) {
        this.comp = component.plus(ActivityModule(this));
    }

    override val component: HomeComponent
        get() = comp


    inner class HomePagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment? {
            return when(position){
                0 -> RecentsFragment()
                1 -> ReposFragment()
                2 -> PullRequestsFragment()
                else -> null
            }
        }

        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence {
            return when(position){
                0 -> getString(R.string.tab_recents)
                1 -> getString(R.string.tab_repositories)
                2 -> getString(R.string.tab_pullrequests)
                else -> ""
            }
        }
    }

}