package com.ftinc.gitissues.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by r0adkll on 11/2/16.
 */

interface GithubAPI {

    @GET("user")
    fun getUser() : Observable<User>

    @GET("user/repos?per_page=100")
    fun getRepositories() : Observable<List<Repository>>

    @GET("users/{user}/repos")
    fun getUserRepositories(@Path("user") user: String) : Observable<List<Repository>>

    @GET("issues")
    fun getAllIssues() : Observable<List<Issue>>

    @GET("repos/{owner}/{repo}/issues")
    fun getIssues(@Path("owner") owner: String,
                  @Path("repo") repo: String) : Observable<List<Issue>>

    @GET("repos/{owner}/{repo}/milestones")
    fun getMilestones(@Path("owner") owner: String,
                      @Path("repo") repo: String) : Observable<List<Milestone>>

    @GET("repos/{owner}/{repo}/labels")
    fun getLabels(@Path("owner") owner: String,
                  @Path("repo") repo: String) : Observable<List<Label>>

    @GET("repos/{owner}/{repo}/issues/comments")
    fun getAllComments(@Path("owner") owner: String,
                       @Path("repo") repo: String) : Observable<List<Comment>>

    @GET("repos/{owner}/{repo}/issues/{number}/comments")
    fun getComments(@Path("owner") owner: String,
                    @Path("repo") repo: String,
                    @Path("number") issueNumber: Int) : Observable<List<Comment>>

    @GET("repos/{owner}/{repo}/issues/events")
    fun getAllEvents(@Path("owner") owner: String,
                     @Path("repo") repo: String) : Observable<List<Event>>

    @GET("repos/{owner}/{repo}/issues/{number}/events")
    fun getEvents(@Path("owner") owner: String,
                    @Path("repo") repo: String,
                    @Path("number") issueNumber: Int) : Observable<List<Comment>>

    @GET("repos/{owner}/{repo}/assignees")
    fun getAssignees(@Path("owner") owner: String,
                     @Path("repo") repo: String) : Observable<List<User>>





}