package com.ftinc.gitissues.api

import android.net.Uri
import com.ftinc.gitissues.util.timeAgo
import com.squareup.moshi.Json
import nz.bradcampbell.paperparcel.PaperParcel
import nz.bradcampbell.paperparcel.PaperParcelable
import java.text.SimpleDateFormat
import java.util.*

/**
 * User Model
 */
@PaperParcel
data class User(val id: Long,
                val login: String,
                val avatar_url: String,
                val type: String,
                val name: String?,
                val company: String?,
                val location: String?,
                val email: String?,
                val bio: String?,
                val public_repos: Int?,
                val followers: Int?,
                val following: Int?,
                val contributions: Int?): PaperParcelable{
    companion object{
        @JvmField val CREATOR = PaperParcelable.Creator(User::class.java)
    }
}

/**
 * Repository Model
 */
data class Repository(val id: Long,
                      val owner: User,
                      val name: String,
                      val full_name: String,
                      val description: String,
                      val private: Boolean,
                      val fork: Boolean,
                      val forks_count: Int,
                      val stargazers_count: Int,
                      val watchers_count: Int,
                      val size: Int,
                      val default_branch: String,
                      val open_issues_count: Int,
                      val has_issues: Boolean,
                      val pushed_at: String,
                      val created_at: String,
                      val updated_at: String)

/**
 * Issue Model
 */
@PaperParcel
data class Issue(val id: Long,
                 val repository_url: String,
                 val comments_url: String,
                 val events_url: String,
                 val number: Int,
                 val title: String,
                 val body: String,
                 val body_text: String,
                 val body_html: String,
                 val state: String,
                 val user: User,
                 val labels: List<Label>,
                 val assignee: User?,
                 val milestone: Milestone?,
                 val locked: Boolean,
                 val comments: Int,
                 val closed_at: String?,
                 val created_at: String,
                 val updated_at: String?,
                 val closed_by: User?,
                 val pull_request: PR?): PaperParcelable{
    companion object{
        @JvmField val CREATOR = PaperParcelable.Creator(Issue::class.java)
    }

    fun getRepository(): Repo{
        val (repo, owner) = Uri.parse(repository_url).pathSegments.reversed()
        return Repo(owner, repo)
    }
}

data class Repo(val owner: String, val repo: String)

@PaperParcel
data class PR(val url: String)

/**
 * An issue edit object to make changes to an issue
 */
data class IssueEdit(val title: String,
                     val body: String,
                     val state: String,
                     val milestone: Int?,
                     val labels: List<String>?,
                     val assignees: List<String>?)

/**
 * Label Model
 */
@PaperParcel
data class Label(val id: Long,
                 val url: String,
                 val name: String,
                 val color: String,
                 val isDefault: Boolean)

/**
 * Label editing model
 */
data class LabelEdit(val name: String,
                     val color: String)

/**
 * Milestone Model
 */
@PaperParcel
data class Milestone(val id: Long,
                     val number: Int,
                     val title: String,
                     val state: String?,
                     val description: String,
                     val creator: User,
                     val open_issues: Int,
                     val closed_issues: Int,
                     val created_at: String,
                     val updated_at: String,
                     val closed_at: String?,
                     val due_on: String?)

data class MilestoneEdit(val title: String,
                         val state: String?,
                         val description: String,
                         val due_on: String?)

/**
 * Issue Comment Model
 */
data class Comment(val id: Long,
                   val body: String,
                   val body_text: String,
                   val body_html: String,
                   val user: User,
                   val created_at: String,
                   val updated_at: String?,
                   val reactions: Reactions)

/**
 * The new comment object to create a new comment on an
 * issue with
 */
data class CommentEdit(val body: String)

/**
 * Reactions Model
 */
data class Reactions(val total_count: Int,
                     @Json(name = "+1") val plusOne: Int,
                     @Json(name = "-1") val minusOne: Int,
                     val laugh: Int,
                     val confused: Int,
                     val heart: Int,
                     val hooray: Int)

/**
 * Event Constants
 */
enum class Events(val event: String){
    CLOSED("closed"),
    REOPENED("reopened"),
    SUBSCRIBED("subscribed"),
    MERGED("merged"),
    REFERENCED("referenced"),
    MENTIONED("mentioned"),
    ASSIGNED("assigned"),
    UNASSIGNED("unassigned"),
    LABELED("labeled"),
    UNLABELED("unlabeled"),
    MILESTONED("milestoned"),
    DEMILESTONED("demilestoned"),
    RENAMED("renamed"),
    LOCKED("locked"),
    UNLOCKED("unlocked"),
    HEAD_REF_DELETED("head_ref_deleted"),
    HEAD_REF_RESTORED("head_ref_restored");

    companion object {
        fun find(tag: String): Events = Events.values().first { it.event == tag }
    }
}

/**
 * Issue Event Model
 */
data class Event(val id: Long,
                 val actor: User,
                 val commit_id: String?,
                 val commit_url: String?,
                 val event: String,
                 val created_at: String,
                 val label: Label?,
                 val assignee: User?,
                 val assigner: User?,
                 val milestone: Milestone?,
                 val rename: Rename?)

data class Rename(val from: String,
                  val to: String)

/**
 * Pull Request Model
 */
data class PullRequest(val id: Long,
                       val number: Int,
                       val state: String,
                       val title: String,
                       val body: String,
                       val assignee: User?,
                       val milestone: Milestone?,
                       val locked: Boolean,
                       val created_at: String,
                       val updated_at: String,
                       val closed_at: String?,
                       val merged_at: String?,
                       val head: Branch,
                       val base: Branch,
                       val user: User)

/**
 * Branch Representation Model
 */
data class Branch(val label: String,
                  val ref: String,
                  val sha: String,
                  val user: User,
                  val repo: Repository)

/**
 * Extension function to convert Github date strings into java date objects
 */
fun String.toGithubDate(): Date? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(this)
fun String.githubTimeAgo(): String = this.toGithubDate()?.timeAgo()?: "a momment ago"