package com.ftinc.gitissues.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by r0adkll on 11/2/16.
 */

interface GithubAPI {

    @GET("user")
    fun getUser() : Observable<User>

    @GET("user/repos?per_page=100&affiliation=owner,collaborator")
    fun getRepositories() : Observable<List<Repository>>

    @GET("users/{user}/repos")
    fun getUserRepositories(@Path("user") user: String) : Observable<List<Repository>>

    @GET("issues?sort=updated&state=all")
    fun getAllIssues(@Query("filter") filter: String?) : Observable<List<Issue>>

    @GET("repos/{owner}/{repo}/issues")
    fun getIssues(@Path("owner") owner: String,
                  @Path("repo") repo: String) : Observable<List<Issue>>

    @GET("repos/{owner}/{repo}/issues/{number}/labels")
    fun getLabelsOnIssue(@Path("owner") owner: String,
                         @Path("repo") repo: String,
                         @Path("number") issueNumber: Int) : Observable<List<Label>>

    @GET
    fun getEventsOnIssue(@Url url: String): Observable<List<Event>>

    @GET("repos/{owner}/{repo}/issues/{number}/events")
    fun getEventsOnIssue(@Path("owner") owner: String,
                         @Path("repo") repo: String,
                         @Path("number") issueNumber: Int) : Observable<List<Event>>

    @GET
    fun getCommentsOnIssue(@Url url: String): Observable<List<Comment>>

    @GET("repos/{owner}/{repo}/issues/{number}/comments")
    fun getCommentsOnIssue(@Path("owner") owner: String,
                           @Path("repo") repo: String,
                           @Path("number") issueNumber: Int) : Observable<List<Comment>>

    @GET("repos/{owner}/{repo}/milestones")
    fun getMilestones(@Path("owner") owner: String,
                      @Path("repo") repo: String) : Observable<List<Milestone>>

    @GET("repos/{owner}/{repo}/labels")
    fun getLabels(@Path("owner") owner: String,
                  @Path("repo") repo: String) : Observable<List<Label>>

    @GET("repos/{owner}/{repo}/issues/comments")
    fun getAllComments(@Path("owner") owner: String,
                       @Path("repo") repo: String) : Observable<List<Comment>>

    @GET("repos/{owner}/{repo}/issues/events")
    fun getAllEvents(@Path("owner") owner: String,
                     @Path("repo") repo: String) : Observable<List<Event>>

    @GET("repos/{owner}/{repo}/assignees")
    fun getAssignees(@Path("owner") owner: String,
                     @Path("repo") repo: String) : Observable<List<User>>

    @GET("repos/{owner}/{repo}/pulls")
    fun getPullRequests(@Path("owner") owner: String,
                        @Path("repo") repo: String,
                        @Query("state") state: String?,
                        @Query("sort") sort: String?) : Observable<List<PullRequest>>


    /***********************************************************************************************
     *
     * Search APIs
     *
     */

    class SearchResult<out T>(val total_count: Int,
                              val incomplete_results: Boolean,
                              val items: List<T>)

    @GET("search/issues")
    fun searchIssues(@Query("q") query: String,
                     @Query("sort") sort: String?,
                     @Query("order") order: String?) : Observable<SearchResult<Issue>>

    @GET("search/issues?q=type:pr+involves:r0adkll")
    fun searchPullRequests() : Observable<SearchResult<Issue>>

}