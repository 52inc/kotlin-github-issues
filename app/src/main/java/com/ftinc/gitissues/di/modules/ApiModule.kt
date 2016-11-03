package com.ftinc.gitissues.di.modules

import com.ftinc.gitissues.BuildConfig
import com.ftinc.gitissues.api.GithubAPI
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by r0adkll on 11/2/16.
 */
@Module
class ApiModule{

    @Provides @Singleton
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor {
                    it.proceed(it.request().newBuilder()
                            .addHeader("Accept", "application/vnd.github.v3+json")
                            .addHeader("Authorization", "token ${BuildConfig.API_TOKEN}")
                            .build())
                }
                .addInterceptor(HttpLoggingInterceptor().setLevel(if(BuildConfig.DEBUG) Level.BODY else Level.NONE))
                .build()
    }

    @Provides @Singleton
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(httpClient).build()
    }

    @Provides @Singleton
    fun provideService(retrofit: Retrofit): GithubAPI {
        return retrofit.create(GithubAPI::class.java)
    }

}