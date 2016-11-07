package com.ftinc.gitissues.util

import android.support.v7.widget.RecyclerView

import com.ftinc.gitissues.ui.adapter.ListRecyclerAdapter
import com.ftinc.gitissues.ui.adapter.delegate.BaseIssueMessage

import java.util.ArrayList
import java.util.Collections

/**
 * Project: ClearChatAndroid
 * Package: com.clearchat.app.presentation.util
 * Created by drew.heavner on 8/30/16.
 */

object RecyclerViewUtils {

    fun <M : BaseIssueMessage> applyDynamicChanges(/*recycler: RecyclerView,*/
                                                   adapter: ListRecyclerAdapter<M, out RecyclerView.ViewHolder>,
                                                   newItems: List<M>) {
        val originalItems = ArrayList(adapter.items)

        // Check empty case
        if (originalItems.isEmpty()) {
            adapter.addAll(newItems)
            adapter.notifyDataSetChanged()
        } else {

            // iterate through old and new items and calculate changes
            for (i in newItems.indices) {
                val newItem = newItems[i]

                var hasOldItem = false
                // Now iterate through old items to see if exists
                for (j in originalItems.indices) {
                    val oldItem = originalItems[j]

                    if (oldItem.getId() == newItem.getId()) {
                        hasOldItem = true
                        if (oldItem.hashCode() != newItem.hashCode()) {
                            // Old and new items are different, replace and update
                            adapter[j] = newItem
                        }

                        // Remove collision from originalItems array for efficiency and remove operations
                        originalItems.remove(oldItem)

                        // else ignore the new item since its an unchanged duplicate
                        break
                    }
                }

                if (!hasOldItem) {
                    // New item! insert it
                    adapter.add(newItem)
//                    recycler.smoothScrollToPosition(adapter.itemCount-1)
                }

            }

            // if there are items in the "original" list, remove them from the adapter
            if (!originalItems.isEmpty()) {
                for (i in originalItems.indices) {
                    val item = originalItems[i]
                    adapter.remove(item)
                }
            }

        }

    }

}
