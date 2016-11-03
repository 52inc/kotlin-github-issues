package com.ftinc.gitissues.di.modules

import android.content.Context
import com.ftinc.gitissues.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by r0adkll on 11/2/16.
 */
@Module
class AppModule(val app: App) {

    @Provides @Singleton
    fun provideContext(): Context = app

}