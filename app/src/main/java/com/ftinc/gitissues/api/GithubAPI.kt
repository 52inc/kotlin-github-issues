package com.ftinc.gitissues.api

import io.reactivex.Observable
import retrofit2.http.*

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
     * Milestones
     *
     */

    @GET("repos/{owner}/{repo}/milestones")
    fun getMilestones(@Path("owner") owner: String,
                      @Path("repo") repo: String) : Observable<List<Milestone>>

    @POST("repos/{owner}/{repo}/milestones")
    fun createMilestones(@Path("owner") owner: String,
                         @Path("repo") repo: String,
                         @Body body: MilestoneEdit): Observable<Milestone>

    @PATCH("repos/{owner}/{repo}/milestones/{number}")
    fun updateMilestone(@Path("owner") owner: String,
                        @Path("repo") repo: String,
                        @Path("number") number: Int,
                        @Body body: MilestoneEdit): Observable<Milestone>

    @DELETE("repos/{owner}/{repo}/milestones/{number}")
    fun deleteMilestone(@Path("owner") owner: String,
                        @Path("repo") repo: String,
                        @Path("number") number: Int): Observable<Void>

    /***********************************************************************************************
     *
     * Labels
     *
     */

    @GET("repos/{owner}/{repo}/labels")
    fun getLabels(@Path("owner") owner: String,
                  @Path("repo") repo: String) : Observable<List<Label>>

    @POST("repos/{owner}/{repo}/labels")
    fun createLabel(@Path("owner") owner: String,
                    @Path("repo") repo: String,
                    @Body body: LabelEdit): Observable<Label>

    @PATCH("repos/{owner}/{repo}/labels/{name}")
    fun editLabel(@Path("owner") owner: String,
                  @Path("repo") repo: String,
                  @Path("name") name: String,
                  @Body body: LabelEdit): Observable<Label>

    @DELETE("repos/{owner}/{repo}/labels/{name}")
    fun deleteLabel(@Path("owner") owner: String,
                    @Path("repo") repo: String,
                    @Path("name") name: String): Observable<Label>

    /***********************************************************************************************
     *
     * Issues
     *
     */

    @GET("issues?sort=updated&state=all")
    fun getAllIssues(@Query("filter") filter: String?) : Observable<List<Issue>>

    @GET("repos/{owner}/{repo}/issues")
    fun getIssues(@Path("owner") owner: String,
                  @Path("repo") repo: String) : Observable<List<Issue>>

    @POST("repos/{owner}/{repo}/issues")
    fun createIssue(@Path("owner") owner: String,
                    @Path("repo") repo: String,
                    @Body body: IssueEdit) : Observable<Issue>

    @PATCH("repos/{owner}/{repo}/issues")
    fun editIssue(@Path("owner") owner: String,
                  @Path("repo") repo: String,
                  @Body body: IssueEdit) : Observable<Issue>

    @GET("repos/{owner}/{repo}/issues/{number}/labels")
    fun getLabelsOnIssue(@Path("owner") owner: String,
                         @Path("repo") repo: String,
                         @Path("number") issueNumber: Int) : Observable<List<Label>>

    @POST("repos/{owner}/{repo}/issues/{number}/labels")
    fun addLabelsToIssue(@Path("owner") owner: String,
                         @Path("repo") repo: String,
                         @Path("number") issueNumber: Int,
                         @Body labels: List<String>) : Observable<List<Label>>

    /*
     * Events
     */

    @GET
    fun getEventsOnIssue(@Url url: String): Observable<List<Event>>

    @GET("repos/{owner}/{repo}/issues/{number}/events")
    fun getEventsOnIssue(@Path("owner") owner: String,
                         @Path("repo") repo: String,
                         @Path("number") issueNumber: Int) : Observable<List<Event>>

    /*
     * Comments
     */

    @GET
    fun getCommentsOnIssue(@Url url: String): Observable<List<Comment>>

    @GET("repos/{owner}/{repo}/issues/{number}/comments")
    fun getCommentsOnIssue(@Path("owner") owner: String,
                           @Path("repo") repo: String,
                           @Path("number") issueNumber: Int) : Observable<List<Comment>>

    @POST("repos/{owner}/{repo}/issues/{number}/comments")
    fun createComment(@Path("owner") owner: String,
                      @Path("repo") repo: String,
                      @Path("number") issueNumber: Int,
                      @Body body: CommentEdit): Observable<Comment>

    @PATCH("repos/{owner}/{repo}/issues/{number}/comments/{id}")
    fun editComment(@Path("owner") owner: String,
                    @Path("repo") repo: String,
                    @Path("number") issueNumber: Int,
                    @Path("id") commentId: Int,
                    @Body body: CommentEdit): Observable<Comment>

    @DELETE("repos/{owner}/{repo}/issues/{number}/comments/{id}")
    fun deleteComment(@Path("owner") owner: String,
                      @Path("repo") repo: String,
                      @Path("number") issueNumber: Int,
                      @Path("id") commentId: Int): Observable<Void>




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