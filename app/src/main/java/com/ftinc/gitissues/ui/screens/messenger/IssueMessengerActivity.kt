package com.ftinc.gitissues.ui.screens.messenger

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.TransitionRes
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.*
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.bindView
import butterknife.bindViews
import com.ftinc.gitissues.App
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.*
import com.ftinc.gitissues.di.components.AppComponent
import com.ftinc.gitissues.ui.BaseActivity
import com.ftinc.gitissues.ui.adapter.AssigneeAdapter
import com.ftinc.gitissues.ui.adapter.LabelAdapter
import com.ftinc.gitissues.ui.adapter.MessengerAdapter
import com.ftinc.gitissues.ui.adapter.MilestoneAdapter
import com.ftinc.gitissues.ui.adapter.delegate.BaseIssueMessage
import com.ftinc.gitissues.ui.adapter.delegate.CommentIssueMessage
import com.ftinc.gitissues.ui.widget.LabelEditor
import com.ftinc.gitissues.ui.widget.LabelView
import com.ftinc.gitissues.ui.widget.MarkdownEditor
import com.ftinc.gitissues.util.*
import com.ftinc.kit.util.UIUtils
import com.ftinc.kit.util.Utils
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
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
    val appbar: Toolbar by bindView(R.id.appbar)

    val status: LabelView by bindView(R.id.status)

    val refreshLayout: SwipeRefreshLayout by bindView(R.id.refresh_layout)
    val recycler: RecyclerView by bindView(R.id.recycler)
    val editor: MarkdownEditor by bindView(R.id.editor)
    val itemEditor: LabelEditor by bindView(R.id.item_editor)

    val fab: FloatingActionButton by bindView(R.id.fab)
    val scrim: View by bindView(R.id.results_scrim)
    val issueActionContainer: FrameLayout by bindView(R.id.issue_actions_container)
    val inputScrim: View by bindView(R.id.input_scrim)

    val actions: List<TextView> by bindViews(R.id.action_labels, R.id.action_milestones,
            R.id.action_assignees, R.id.action_new_comment)

    private lateinit var adapter: MessengerAdapter
    private lateinit var issue: Issue
    private lateinit var labelAdapter: LabelAdapter
    private lateinit var milestoneAdapter: MilestoneAdapter
    private lateinit var assigneeAdapter: AssigneeAdapter
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

        appbar.setNavigationOnClickListener {
            supportFinishAfterTransition()
        }

        adapter = MessengerAdapter(this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
        (recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val actionBarSize = UIUtils.getActionBarSize(this@IssueMessengerActivity).toFloat() / 2f
                val percent = Utils.clamp(Math.abs(recyclerView?.computeVerticalScrollOffset()?.toFloat() ?: 0f), 0f, actionBarSize) / actionBarSize
                val color = android.support.v4.graphics.ColorUtils.setAlphaComponent(color(R.color.colorPrimary), (percent * 255).toInt())
                appbar.setBackgroundColor(color)
            }
        })

        labelAdapter = LabelAdapter(this)
        milestoneAdapter = MilestoneAdapter(this)
        assigneeAdapter = AssigneeAdapter(this)

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

        status.setOnClickListener {
            val menu = PopupMenu(this, it, Gravity.END or Gravity.TOP)
            menu.inflate(R.menu.status_menu)
            val toggle = menu.menu.findItem(R.id.action_toggle)
            toggle.setTitle(if(status.text.toString().equals("open", true)) R.string.close_issue else R.string.open_issue)
            menu.setOnMenuItemClickListener {
                if(it.itemId == R.id.action_toggle){
                    presenter.toggleStatus()
                }
                true
            }
            menu.show()
        }

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
        }else if(itemEditor.visibility == View.VISIBLE){
            fab.visible()
            itemEditor.hide()
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
                itemEditor.setTitle(getString(R.string.action_labels))
                itemEditor.setEmptyView(R.string.empty_message_generic, R.drawable.ic_empty_github)
                itemEditor.setAdapter(labelAdapter)
                itemEditor.setOnItemSaveListener(object : LabelEditor.OnItemSaveListener{
                    override fun onItemsSaved() {
                        presenter.updateLabels(labelAdapter.selectedLabels)
                    }
                })

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
                itemEditor.visible()
                inputScrim.visible()
            }
            R.id.action_milestones -> {
                itemEditor.setTitle(getString(R.string.action_milestones))
                itemEditor.setEmptyView(R.string.empty_message_generic, R.drawable.ic_empty_github)
                itemEditor.setAdapter(milestoneAdapter)
                itemEditor.setOnItemSaveListener(object : LabelEditor.OnItemSaveListener{
                    override fun onItemsSaved() {
                        presenter.updateMilestone(milestoneAdapter.currentMilestone)
                    }
                })

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
                itemEditor.visible()
                inputScrim.visible()
            }
            R.id.action_assignees -> {
                itemEditor.setTitle(getString(R.string.action_assignees))
                itemEditor.setEmptyView(R.string.empty_message_generic, R.drawable.ic_empty_github)
                itemEditor.setAdapter(assigneeAdapter)
                itemEditor.setOnItemSaveListener(object : LabelEditor.OnItemSaveListener{
                    override fun onItemsSaved() {
                        presenter.updateAssignees(assigneeAdapter.selectedUsers)
                    }
                })

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
                itemEditor.visible()
                inputScrim.visible()
            }
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

    override fun setEditableLabels(labels: List<Label>?, selectedMap: MutableMap<Label, Boolean>) {
        labelAdapter.clear()
        labelAdapter.addAll(labels ?: listOf())
        labelAdapter.checkedState.putAll(selectedMap)
        labelAdapter.notifyDataSetChanged()
    }

    override fun setEditableMilestones(milestones: List<Milestone>?, currentMilestone: Milestone?) {
        milestoneAdapter.clear()
        milestoneAdapter.addAll(milestones)
        milestoneAdapter.currentMilestone = currentMilestone
        milestoneAdapter.notifyDataSetChanged()
    }

    override fun setEditableAssignees(assignees: List<User>?, selectedMap: MutableMap<User, Boolean>) {
        assigneeAdapter.clear()
        assigneeAdapter.addAll(assignees)
        assigneeAdapter.checkedState.putAll(selectedMap)
        assigneeAdapter.notifyDataSetChanged()
    }

    override fun setMessengerItems(items: List<BaseIssueMessage>) {
        RecyclerViewUtils.applyDynamicChanges(adapter, items)
    }

    override fun updateIssueMessengerItem(item: BaseIssueMessage) {
        if(adapter.itemCount > 0)
            adapter[0] = item
        else
            adapter.add(item)
    }

    override fun appendComment(comment: CommentIssueMessage?) {
        adapter.add(comment as BaseIssueMessage)
        recycler.scrollToPosition(adapter.itemCount-1)
    }

}
