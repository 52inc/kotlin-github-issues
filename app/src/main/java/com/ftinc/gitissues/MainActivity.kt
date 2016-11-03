package com.ftinc.gitissues

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.TextView
import butterknife.bindView
import com.ftinc.gitissues.api.GithubAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    val text: TextView by bindView(R.id.text)

    @Inject
    lateinit var api: GithubAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        setContentView(R.layout.activity_main)

        api.getRepositories()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ repos ->
                val newText = repos
                        .filter { it.open_issues_count > 0 && it.owner.login != "Censio" }
                        .sortedByDescending { it.stargazers_count }
                        .map { "${it.full_name} - Stars(${it.stargazers_count}) - Issues(${it.open_issues_count})" }
                        .fold("", {
                            repoText, repo ->
                            "$repoText\n$repo"
                        })
                text.text = newText
            }, { error ->
                Timber.e(error, "Error downloading Repos")
                Snackbar.make(text, error.message ?: "", Snackbar.LENGTH_SHORT).show()
            })

    }
}
