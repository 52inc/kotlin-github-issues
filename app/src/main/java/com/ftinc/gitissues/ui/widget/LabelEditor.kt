package com.ftinc.gitissues.ui.widget

import `in`.uncod.android.bypass.Bypass
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout
import android.widget.SearchView
import butterknife.bindView
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Label
import com.ftinc.gitissues.ui.adapter.LabelAdapter
import com.ftinc.gitissues.util.AnimUtils
import com.ftinc.gitissues.util.gone
import com.ftinc.kit.widget.EmptyView

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.ui.widget
 * Created by drew.heavner on 11/10/16.
 */

class LabelEditor : RelativeLayout,SearchView.OnQueryTextListener {

    val toolbar: Toolbar by bindView(R.id.toolbar)
    val searchView: SearchView by bindView(R.id.search_view)
    val recyclerView: RecyclerView by bindView(R.id.recycler)
    val emptyView: EmptyView by bindView(R.id.empty_view)
    val adapter: LabelAdapter

    lateinit var labelSaveListener: OnLabelsSelectedListener

    /***********************************************************************************************
     *
     * Constructors
     *
     */

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        adapter = LabelAdapter(context)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // Setup Recycler View
        adapter.setEmptyView(emptyView)
        recyclerView.adapter = adapter

        // Setup Search View
        searchView.setOnQueryTextListener(this)
        searchView.setOnQueryTextFocusChangeListener { view, b ->
            if(!b){
                adapter.clearFilter()
            }
        }

        // Setup toolbar
        toolbar.inflateMenu(R.menu.label_editor)
        toolbar.setNavigationOnClickListener {
            hide()
        }
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_save -> {
                    // Get the list of selected labels, and return them and hide this editor
                    val labels = adapter.selectedLabels
                    labelSaveListener?.onLabelsSaved(labels)
                    hide()
                    true
                }
                else -> false
            }
        }

    }

    /***********************************************************************************************
     *
     * Public Helper Methods
     *
     */

    fun setOnLabelsSelectedListener(listener: OnLabelsSelectedListener){
        labelSaveListener = listener
    }

    fun setLabels(labels: List<Label>, selected: Map<Label, Boolean>){
        adapter.clear()
        adapter.addAll(labels)
        adapter.checkedState.putAll(selected)
        adapter.notifyDataSetChanged()
    }

    fun hide(){
        animate()
                .translationY(height.toFloat())
                .setDuration(300)
                .setInterpolator(AnimUtils.getFastOutSlowInInterpolator(context))
                .withEndAction {
                    gone()
                    translationY = 0f
                }
                .start()
    }

    /***********************************************************************************************
     *
     * Filter change methods
     *
     */

    override fun onQueryTextSubmit(query: String?): Boolean {
        adapter.filter(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter(newText)
        return true
    }

    /***********************************************************************************************
     *
     * Inner Classes and Interfaces
     *
     */

    interface OnLabelsSelectedListener{
        fun onLabelsSaved(labels: List<Label>)
    }

}
