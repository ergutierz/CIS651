package com.example.cis651syracuse.project2.util

import android.content.Context
import android.content.Intent
import com.example.cis651syracuse.project2.view.MovieAboutActivity
import com.example.cis651syracuse.project2.view.MovieListDetailActivity
import com.example.cis651syracuse.project2.view.MovieViewPagerActivity

object NavUtil {
    fun navigateTo(context: Context, currentScreen: Screen, screen: Screen): Intent? {
        return when (screen) {
            Screen.Movies -> MovieListDetailActivity.newIntent(context)
            Screen.MovieDetail -> MovieListDetailActivity.newIntent(context)
            Screen.Dashboard -> MovieAboutActivity.newIntent(context)
            Screen.MovieViewPager -> MovieViewPagerActivity.newIntent(context)
        }.takeUnless { currentScreen == screen }
    }
}