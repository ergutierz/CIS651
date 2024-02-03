package com.example.cis651syracuse.project2.util

sealed class Screen(val title: String) {
    object Dashboard : Screen("About")
    object Movies : Screen("Movies")
    object MovieDetail : Screen("Movie Detail")
    object MovieViewPager : Screen("Movie Carousel")

    val isDashboard: Boolean get() = this is Dashboard
}