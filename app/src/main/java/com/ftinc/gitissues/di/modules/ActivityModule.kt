package com.ftinc.gitissues.di.modules

import android.app.Activity
import com.ftinc.gitissues.di.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Created by r0adkll on 11/3/16.
 */
@Module
class ActivityModule(val activity: Activity){

    @Provides @ActivityScope
    fun provideActivityContext(): Activity = activity

}