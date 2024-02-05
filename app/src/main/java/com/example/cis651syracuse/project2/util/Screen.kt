package com.example.cis651syracuse.project2.util

import androidx.annotation.StringRes
import com.example.cis651syracuse.R

sealed class Screen(@StringRes val title: Int) {
    object Dashboard : Screen(R.string.about)
    object Movies : Screen(R.string.movies)
    object MovieDetail : Screen(R.string.movie_detail)
    object MovieViewPager : Screen(R.string.movie_carousel)

    val isDashboard: Boolean get() = this is Dashboard
}