package com.ftinc.gitissues.ui.screens.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.bindView
import com.bumptech.glide.Glide
import com.ftinc.gitissues.App
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Issue
import com.ftinc.gitissues.api.IssueParcel
import com.ftinc.gitissues.api.Label
import com.ftinc.gitissues.di.components.AppComponent
import com.ftinc.gitissues.ui.BaseActivity
import com.ftinc.gitissues.ui.adapter.MessengerAdapter
import com.ftinc.gitissues.ui.adapter.delegate.BaseIssueMessage
import com.ftinc.gitissues.ui.widget.LabelView
import com.ftinc.kit.widget.BezelImageView
import javax.inject.Inject

/**
 * Created by r0adkll on 11/4/16.
 */

class IssueMessengerActivity: BaseActivity(), IssueMessengerView{

    companion object{
        @JvmStatic val EXTRA_ISSUE = "com.r0adkll.gitissues.intent.EXTRA_ISSUE"

        fun createIntent(context: Context, issue: Issue): Intent{
            val intent: Intent =  Intent(context, IssueMessengerActivity::class.java)
            intent.putExtra(EXTRA_ISSUE, IssueParcel(issue))
            return intent
        }
    }

    /***********************************************************************************************
     *
     * Variables
     *
     */

    val status: LabelView by bindView(R.id.status)
    val number: TextView by bindView(R.id.number)
    val issueTitle: TextView by bindView(R.id.issue_title)
    val ownerAvatar: BezelImageView by bindView(R.id.owner_avatar)
    val ownerName: TextView by bindView(R.id.owner_name)
    val openDate: TextView by bindView(R.id.open_date)
    val labelContainer: LinearLayout by bindView(R.id.label_container)
    val recycler: RecyclerView by bindView(R.id.recycler)

    lateinit var adapter: MessengerAdapter
    lateinit var issue: Issue

    @Inject
    lateinit var presenter: IssueMessengerPresenter

    /***********************************************************************************************
     *
     * Lifecycle Methods
     *
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issue_messenger)

        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        toolbar?.setNavigationOnClickListener {
            supportFinishAfterTransition()
        }

        adapter = MessengerAdapter(this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadIssueContent()
    }

    /***********************************************************************************************
     *
     * Base Methods
     *
     */

    override fun setupComponent(component: AppComponent) {
        intent?.let {
            val ip: IssueParcel = it.getParcelableExtra(EXTRA_ISSUE)
            issue = ip.data
        }

        App.appComponent.plus(IssueMessengerModule(issue, this))
                .inject(this)
    }

    /***********************************************************************************************
     *
     * View Methods
     *
     */

    override fun setStatus(status: String) {
        this.status.text = status
    }

    override fun setNumber(number: String) {
        this.number.text = number
    }

    override fun setIssueTitle(title: String) {
        issueTitle.text = title
    }

    override fun setOwnerAvatar(url: String) {
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.dr_avatar_default)
                .crossFade()
                .into(ownerAvatar)
    }

    override fun setOwnerName(name: String) {
        ownerName.text = name
    }

    override fun setOpenDate(date: String) {
        openDate.text = date
    }

    override fun setLabels(labels: List<Label>) {
        labelContainer.removeAllViews()
        labels.map { LabelView(this, it) }
                .forEach { labelContainer.addView(it) }
    }

    override fun setMessengerItems(items: List<BaseIssueMessage>) {
        adapter.clear()
        adapter.addAll(items)
        adapter.notifyDataSetChanged()
    }



}