package com.ftinc.gitissues.ui.screens.messenger

import android.os.Bundle
import com.ftinc.gitissues.R
import com.ftinc.gitissues.di.components.AppComponent
import com.ftinc.gitissues.ui.BaseActivity

/**
 * Created by r0adkll on 11/4/16.
 */

class IssueMessengerActivity: BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issue_messenger)
    }

    override fun setupComponent(component: AppComponent) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}