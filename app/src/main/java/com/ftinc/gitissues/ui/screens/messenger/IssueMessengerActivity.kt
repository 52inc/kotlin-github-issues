package com.ftinc.gitissues.ui.screens.messenger

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.TransitionRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.support.v7.widget.Toolbar
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.util.SparseArray
import android.view.View
import android.widget.*
import butterknife.bindView
import butterknife.bindViews
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
import com.ftinc.gitissues.ui.adapter.delegate.CommentIssueMessage
import com.ftinc.gitissues.ui.widget.LabelEditor
import com.ftinc.gitissues.ui.widget.LabelView
import com.ftinc.gitissues.ui.widget.MarkdownEditor
import com.ftinc.gitissues.util.*
import com.ftinc.kit.util.UIUtils
import com.ftinc.kit.util.Utils
import com.ftinc.kit.widget.BezelImageView
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by r0adkll on 11/4/16.
 */

class IssueMessengerActivity: BaseActivity(), IssueMessengerView, View.OnClickListener {

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

    val rootLayout: CoordinatorLayout by bindView(R.id.root_layout)
    val appBarLayout: AppBarLayout by bindView(R.id.appbar_layout)
    val collapsingAppBar: CollapsingToolbarLayout by bindView(R.id.collapsing_toolbar)
    val appbar: Toolbar by bindView(R.id.appbar)

    val collapsedTitleInfo: RelativeLayout by bindView(R.id.collapsed_title_info)
    val collapsedIssueTitle: TextView by bindView(R.id.collapsed_issue_title)
    val collapsedNumber: TextView by bindView(R.id.collapsed_number)

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
    val editor: MarkdownEditor by bindView(R.id.editor)
    val labelEditor: LabelEditor by bindView(R.id.label_editor)

    val fab: FloatingActionButton by bindView(R.id.fab)
    val scrim: View by bindView(R.id.results_scrim)
    val issueActionContainer: FrameLayout by bindView(R.id.issue_actions_container)
    val inputScrim: View by bindView(R.id.input_scrim)

    val actions: List<TextView> by bindViews(R.id.action_labels, R.id.action_milestones,
            R.id.action_assignees, R.id.action_new_comment)

    private lateinit var adapter: MessengerAdapter
    private lateinit var issue: Issue
    private val transitions = SparseArray<Transition>()

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

        val config: SlidrConfig = SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .position(SlidrPosition.LEFT)
                .build()
        Slidr.attach(this, config)

        val backArrow: AnimatedVectorDrawable = getDrawable(R.drawable.avd_back_simple) as AnimatedVectorDrawable
        appbar.navigationIcon = backArrow
        backArrow.start()

        collapsingAppBar.isTitleEnabled = false

        appbar.setNavigationOnClickListener {
            supportFinishAfterTransition()
        }

        appBarLayout.addOnOffsetChangedListener { appBarLayout, i ->
            val total: Float = (appBarLayout.height - appbar.height).toFloat()
            val percent: Float = Math.abs(i).toFloat() / total
            val accelPercent: Float = Utils.clamp(Math.abs(i * 2).toFloat(), 0f, total) / total

            val i2: Float = Math.abs(i * 2).toFloat()
            val p2: Float = Utils.clamp(i2 - total, 0f, total) / total

            // modulate alpha visibility of header views
            val alpha: Float = 1f - accelPercent
            status.alpha = alpha
            ownerAvatar.alpha = alpha
            ownerName.alpha = alpha
            openDate.alpha = alpha
            labelContainer.alpha = alpha
            openedLabel.alpha = alpha
            titleInfo.alpha = alpha

            // Modulate Collapsed title visibility
            collapsedTitleInfo.alpha = p2

        }

        adapter = MessengerAdapter(this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
        (recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        refreshLayout.setOnRefreshListener {
            presenter.loadIssueContent()
        }

        editor.setOnMarkdownSubmitListener(object : MarkdownEditor.OnMarkdownSubmitListener{
            override fun onMarkdownSubmit(markdown: String) {
                presenter.createComment(markdown)
            }
        })

        fab.setOnClickListener {
            TransitionManager.beginDelayedTransition(rootLayout, getTransition(R.transition.search_show_confirm))
            fab.invisible()
            issueActionContainer.visible()
            scrim.visible()
        }

        scrim.setOnClickListener {
            if(issueActionContainer.visibility == View.VISIBLE){
                TransitionManager.beginDelayedTransition(rootLayout, getTransition(R.transition.search_hide_confirm))
                issueActionContainer.gone()
                scrim.gone()
                fab.visible()
            }
        }

        actions.forEach {
            it.setOnClickListener(this)
        }

        labelEditor.setOnLabelsSelectedListener(object : LabelEditor.OnLabelsSelectedListener{
            override fun onLabelsSaved(labels: List<Label>) {
                presenter.updateLabels(labels)
            }
        })

    }

    override fun onResume() {
        super.onResume()
        refreshLayout.isRefreshing = true
        presenter.loadIssueContent()
    }

    override fun onBackPressed() {
        if(editor.visibility == View.VISIBLE) {
            fab.visible()
            editor.hide()
        }else if(labelEditor.visibility == View.VISIBLE){
            fab.visible()
            labelEditor.hide()
        }else if(issueActionContainer.visibility == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(rootLayout, getTransition(R.transition.search_hide_confirm))
            issueActionContainer.gone()
            scrim.gone()
            fab.visible()
        }else {
            super.onBackPressed()
        }
    }

    /**
     * Called when an Issue action is clicked
     */
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.action_labels -> {
                val transition = getTransition(R.transition.issue_label_editor_show)
                transition?.addListener(object : TransitionUtils.TransitionListenerAdapter() {
                    override fun onTransitionEnd(transition: Transition?) {
                        issueActionContainer.gone()
                        scrim.gone()
                        fab.visible()
                        inputScrim.animate()
                                .alpha(0f)
                                .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(this@IssueMessengerActivity))
                                .setDuration(300)
                                .withEndAction {
                                    inputScrim.gone()
                                    inputScrim.alpha = 1f
                                }
                                .start()
                    }
                })
                TransitionManager.beginDelayedTransition(rootLayout, transition)
                labelEditor.visible()
                inputScrim.visible()
            }
            R.id.action_milestones -> null
            R.id.action_assignees -> null
            R.id.action_new_comment -> {
                val transition = getTransition(R.transition.issue_editor_show)
                transition?.addListener(object : TransitionUtils.TransitionListenerAdapter() {
                    override fun onTransitionEnd(transition: Transition?) {
                        issueActionContainer.gone()
                        scrim.gone()
                        fab.visible()
                        inputScrim.animate()
                                .alpha(0f)
                                .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(this@IssueMessengerActivity))
                                .setDuration(300)
                                .withEndAction {
                                    inputScrim.gone()
                                    inputScrim.alpha = 1f
                                }
                                .start()
                    }
                })
                TransitionManager.beginDelayedTransition(rootLayout, transition)
                editor.visible()
                inputScrim.visible()
            }
        }
    }

    internal fun getTransition(@TransitionRes transitionId: Int): Transition? {
        var transition: Transition? = transitions.get(transitionId)
        if (transition == null) {
            transition = TransitionInflater.from(this).inflateTransition(transitionId)
            transitions.put(transitionId, transition)
        }
        return transition
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

    override fun hideInput() {
        editor.hide()
    }

    override fun setStatus(status: String, @ColorRes color: Int) {
        this.status.text = status.toUpperCase()
        this.status.labelColor = color(color)
    }

    override fun setNumber(number: String) {
        this.number.text = number
        this.collapsedNumber.text = number
    }

    override fun setIssueTitle(title: String) {
        issueTitle.text = title
        collapsedIssueTitle.text = title
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

    override fun setEditableLabels(labels: List<Label>?, selectedMap: MutableMap<Label, Boolean>) {
        labelEditor.setLabels(labels ?: listOf(), selectedMap)
    }

    override fun setMessengerItems(items: List<BaseIssueMessage>) {
        RecyclerViewUtils.applyDynamicChanges(adapter, items)
    }

    override fun appendComment(comment: CommentIssueMessage?) {
        adapter.add(comment as BaseIssueMessage)
        recycler.scrollToPosition(adapter.itemCount-1)
    }




}
