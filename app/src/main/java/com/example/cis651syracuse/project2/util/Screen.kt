package com.example.cis651syracuse.project2.util

sealed class Screen(val title: String) {
    object Dashboard : Screen("About")
    object Movies : Screen("Movies")
    object RandomMovie : Screen("Random Movie")

    val isDashboard: Boolean get() = this is Dashboard
    val isMovies: Boolean get() = this is Movies
    val isRandomMovie: Boolean get() = this is RandomMovie
}