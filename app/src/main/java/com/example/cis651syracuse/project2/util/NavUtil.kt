package com.example.cis651syracuse.project2.util

import android.content.Context
import android.content.Intent
import com.example.cis651syracuse.project2.view.DashboardActivity
import com.example.cis651syracuse.project2.view.MovieListActivity
import com.example.cis651syracuse.project2.view.RandomMovieDetailActivity

object NavUtil {
    fun navigateTo(context: Context, currentScreen: Screen, screen: Screen): Intent? {
        return when (screen) {
            Screen.Movies -> MovieListActivity.newIntent(context)
            Screen.Dashboard -> DashboardActivity.newIntent(context)
            Screen.RandomMovie -> RandomMovieDetailActivity.newIntent(context)
        }.takeUnless { currentScreen == screen }
    }
}