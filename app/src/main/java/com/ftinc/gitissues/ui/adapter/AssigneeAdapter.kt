package com.ftinc.gitissues.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftinc.gitissues.api.Label
import com.ftinc.gitissues.api.User
import com.ftinc.gitissues.ui.adapter.viewholder.AssigneeViewHolder
import com.ftinc.kit.adapter.BetterRecyclerAdapter

/**
 * Created by r0adkll on 11/13/16.
 */

class AssigneeAdapter

(activity: Activity) : BetterRecyclerAdapter<User, AssigneeViewHolder>() {

    private var inflater: LayoutInflater
    val checkedState = mutableMapOf<User, Boolean>()
    val selectedUsers: List<User>
        get() {
            return checkedState.filterValues { it }.keys.toList()
        }

    init {
        inflater = activity.layoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AssigneeViewHolder {
        return AssigneeViewHolder.create(inflater, parent)
    }

    override fun onBindViewHolder(vh: AssigneeViewHolder?, i: Int) {
        val user = items[i]
        vh?.bind(user, checkedState.getOrElse(user, { -> false }))

        vh?.itemView?.setOnClickListener {
            toggleState(user, i)
            notifyDataSetChanged()
        }
    }

    override fun clear() {
        super.clear()
        checkedState.clear()
    }

    fun toggleState(user: User, i: Int){
        val state = checkedState.getOrElse(user, { ->
            false
        })
        checkedState.put(user, !state)
        this.notifyItemChanged(i)
    }

}