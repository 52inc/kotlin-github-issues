package com.ftinc.gitissues.ui.screens.messenger

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.support.v7.widget.Toolbar
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.LinearLayout
import android.widget.RelativeLayout
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
import com.ftinc.gitissues.util.dpToPx
import com.ftinc.kit.util.UIUtils
import com.ftinc.kit.util.Utils
import com.ftinc.kit.widget.BezelImageView
import timber.log.Timber
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

    val appBarLayout: AppBarLayout by bindView(R.id.appbar_layout)
    val collapsingAppBar: CollapsingToolbarLayout by bindView(R.id.collapsing_toolbar)
    val appbar: Toolbar by bindView(R.id.toolbar)
    val titleContent: LinearLayout by bindView(R.id.title_content)

    val status: LabelView by bindView(R.id.status)
    val number: TextView by bindView(R.id.number)
    val titleInfo: RelativeLayout by bindView(R.id.title_info)
    val issueTitle: TextView by bindView(R.id.issue_title)
    val ownerAvatar: BezelImageView by bindView(R.id.owner_avatar)
    val ownerName: TextView by bindView(R.id.owner_name)
    val openDate: TextView by bindView(R.id.open_date)
    val openedLabel: TextView by bindView(R.id.opened_label)
    val labelContainer: LinearLayout by bindView(R.id.label_container)

    val refreshLayout: SwipeRefreshLayout by bindView(R.id.refresh_layout)
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

//        setSupportActionBar(appbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val backArrow: AnimatedVectorDrawable = getDrawable(R.drawable.avd_back_simple) as AnimatedVectorDrawable
        appbar.navigationIcon = backArrow
        backArrow.start()

        collapsingAppBar.isTitleEnabled = false

        appbar.setNavigationOnClickListener {
            supportFinishAfterTransition()
        }

        appBarLayout.addOnOffsetChangedListener { appBarLayout, i ->
            val total: Int = appBarLayout.height - appbar.height
            val percent: Float = Math.abs(i).toFloat() / total.toFloat()
            val accelPercent: Float = Utils.clamp(Math.abs(i * 2).toFloat(), 0f, total.toFloat()) / total.toFloat()

            // modulate alpha visibility of header views
            val alpha: Float = 1f - accelPercent
            status.alpha = alpha
            ownerAvatar.alpha = alpha
            ownerName.alpha = alpha
            openDate.alpha = alpha
            labelContainer.alpha = alpha
            openedLabel.alpha = alpha

            // modulate translations
            val translationX = dpToPx(40f) * percent
            titleInfo.translationX = translationX

            // Calculate translationY
            val infoTop: Int = titleInfo.top - titleContent.top
            val top: Int = ((appbar.height/2) - (titleInfo.height/2))
            val translationY: Float = (top - infoTop) * percent

            Timber.d("Title - tY: $translationY, tX: $translationX, contentTop: ${titleContent.top}, contentHeight: ${titleContent.height}, infoTop: ${titleInfo.top}, infoHeight: ${titleInfo.height}")

            titleInfo.translationY = translationY
        }

        adapter = MessengerAdapter(this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
        (recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        refreshLayout.setOnRefreshListener {
            presenter.loadIssueContent()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshLayout.isRefreshing = true
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

    override fun hideLoading() {
        refreshLayout.isRefreshing = false
    }

    override fun setStatus(status: String, @ColorRes color: Int) {
        this.status.text = status.toUpperCase()
        this.status.labelColor = color(color)
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
                .forEach {
                    val lp: LinearLayout.LayoutParams  = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    lp.marginEnd = Utils.dipToPx(this, 8f)
                    labelContainer.addView(it, lp)
                }
    }

    override fun setMessengerItems(items: List<BaseIssueMessage>) {
        adapter.clear()
        adapter.addAll(items)
        adapter.notifyDataSetChanged()
    }



}
