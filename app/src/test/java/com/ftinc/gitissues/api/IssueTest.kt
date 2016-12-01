package com.ftinc.gitissues.api

import android.os.Build
import com.ftinc.gitissues.BuildConfig
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by r0adkll on 11/7/16.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(Build.VERSION_CODES.LOLLIPOP))
class IssueTest {
    @Test
    fun getRepository() {

        val issue: Issue = Issue(0L, "https://api.github.com/repos/octocat/Hello-World",
                "", "", 0, "", "", "", "", "", User(0, "", "", "", "", "", "", "", "", 0, 0, 0, 0), listOf(),
                null, null, null, false, 0, null, "", null, null, null)

        val repo: Repo = issue.getRepository()

        assertEquals("octocat", repo.owner)
        assertEquals("Hello-World", repo.repo)
    }

}