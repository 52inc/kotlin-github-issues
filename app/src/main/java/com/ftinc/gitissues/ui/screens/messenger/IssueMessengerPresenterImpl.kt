package com.ftinc.gitissues.ui.screens.messenger

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

        view.setIssueTitle(issue.title)
        view.setNumber("#${issue.number}")
        view.setOwnerName(issue.user.login)
        view.setOwnerAvatar(issue.user.avatar_url)
        view.setStatus(issue.state)
        view.setOpenDate(issue.created_at.toGithubDate()?.timeAgo().toString())
        view.setLabels(issue.labels)

        // Allways set and show the 'issue' message item
        val issueMsg: IssueMessage = IssueMessage(issue)
        view.setMessengerItems(arrayListOf(issueMsg))

        Observable.combineLatest(api.getCommentsOnIssue(issue.comments_url),
                api.getEventsOnIssue(issue.events_url), BiFunction<List<Comment>, List<Event>, List<BaseIssueMessage>> { t1, t2 ->
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
        }, { error ->
            view.showSnackBar(error)
        })


    }

}