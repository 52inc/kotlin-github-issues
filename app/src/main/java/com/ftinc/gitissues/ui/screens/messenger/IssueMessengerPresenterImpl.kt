package com.ftinc.gitissues.ui.screens.messenger

import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.*
import com.ftinc.gitissues.ui.adapter.delegate.*
import com.ftinc.gitissues.util.timeAgo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import java.util.*

/**
 * Created by r0adkll on 11/6/16.
 */

class IssueMessengerPresenterImpl(val issue: Issue,
                                  val api: GithubAPI,
                                  val view: IssueMessengerView): IssueMessengerPresenter{

    override fun loadIssueContent() {

        // determine status color
        val statusColor: Int = if(issue.state.equals("open", true)) R.color.green_500 else R.color.red_500

        view.setIssueTitle(issue.title)
        view.setNumber("#${issue.number}")
        view.setOwnerName(issue.user.login)
        view.setOwnerAvatar(issue.user.avatar_url)
        view.setStatus(issue.state, statusColor)
        view.setOpenDate(issue.updated_at?.githubTimeAgo().toString())
        view.setLabels(issue.labels)

        // Allways set and show the 'issue' message item
        val issueMsg: IssueMessage = IssueMessage(issue)
        view.setMessengerItems(arrayListOf(issueMsg))

        Observable.combineLatest(
                getCommentsObservable(),
                getEventsObservable(),
                BiFunction<List<Comment>, List<Event>, List<BaseIssueMessage>> { t1, t2 ->
            val items: ArrayList<BaseIssueMessage> = ArrayList()
            items.addAll(t1.map(::CommentIssueMessage))
            items.addAll(t2.map(::EventIssueMessage))
            items
        })
        .flatMap { Observable.fromIterable(it) }
        .sorted { lhs, rhs -> lhs.getCreatedDate().compareTo(rhs.getCreatedDate()) }
        .toList()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ items ->
            items.add(0, IssueMessage(issue))
            view.setMessengerItems(items)
            view.hideLoading()
        }, { error ->
            view.showSnackBar(error)
            view.hideLoading()
        })


    }

    fun getCommentsObservable(): Observable<List<Comment>>{
        return api.getCommentsOnIssue(issue.comments_url)
    }

    fun getEventsObservable(): Observable<List<Event>>{
        return api.getEventsOnIssue(issue.events_url)
                .map { it.filter {
                    val e: Events = Events.find(it.event)
                    e != Events.MENTIONED && e != Events.SUBSCRIBED
                } }
    }

}