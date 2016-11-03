package com.ftinc.gitissues

import android.app.Application
import com.ftinc.gitissues.di.components.AppComponent
import com.ftinc.gitissues.di.components.DaggerAppComponent
import com.ftinc.gitissues.di.modules.ApiModule
import com.ftinc.gitissues.di.modules.AppModule
import timber.log.Timber

/**
 * Created by r0adkll on 11/2/16.
 */

class App : Application(){

    companion object {
        @JvmStatic lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        setupDagger()
        setupLogging()
    }

    fun setupDagger(){
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    fun setupLogging(){
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }else{
            // plant release tree
        }
    }

}