package com.ftinc.gitissues.ui.screens.home

import android.os.Bundle
import com.ftinc.gitissues.R
import com.ftinc.gitissues.di.components.AppComponent
import com.ftinc.gitissues.ui.BaseActivity

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.ui.screens.home
 * Created by drew.heavner on 11/3/16.
 */
class HomeActivity: BaseActivity(){

    /***********************************************************************************************
     *
     * Variables
     *
     */



    /***********************************************************************************************
     *
     * Lifecycle Methods
     *
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    /***********************************************************************************************
     *
     * Base Methods
     *
     */

    override fun setupComponent(component: AppComponent) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}