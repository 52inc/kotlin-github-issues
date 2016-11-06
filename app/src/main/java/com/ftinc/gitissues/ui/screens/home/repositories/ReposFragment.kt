package com.ftinc.gitissues.ui.screens.home.repositories

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import com.ftinc.gitissues.R
import com.ftinc.gitissues.api.Repository
import com.ftinc.gitissues.di.components.HasComponent
import com.ftinc.gitissues.ui.BaseFragment
import com.ftinc.gitissues.ui.screens.home.HomeComponent
import com.ftinc.gitissues.ui.adapter.ReposAdapter
import com.ftinc.kit.widget.EmptyView
import javax.inject.Inject

/**
 * Created by r0adkll on 11/3/16.
 */

class ReposFragment: BaseFragment(), ReposView{

    val recycler: RecyclerView by bindView(R.id.recycler)
    val emptyView: EmptyView by bindView(R.id.empty_view)

    @Inject
    lateinit var presenter: ReposPresenter

    lateinit var adapter: ReposAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = ReposAdapter(activity)
        adapter.setEmptyView(emptyView)
        adapter.setOnItemClickListener { view, repository, i -> showSnackBar(repository.name) }

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadUserRepos()
    }

    override fun setupComponent() {
        (activity as HasComponent<HomeComponent>).component.plus(ReposModule(this))
                .inject(this)
    }

    override fun setRepoItems(items: List<Repository>) {
        adapter.clear()
        adapter.addAll(items)
        adapter.notifyDataSetChanged()
    }

    override fun setLoading(flag: Boolean) {
        when(flag){
            true -> emptyView.setLoading()
            false -> emptyView.setEmpty()
        }
    }
}