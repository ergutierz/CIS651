package com.example.cis651syracuse.project2.repository

import com.example.cis651syracuse.project2.model.PopularMoviesResponse
import com.example.cis651syracuse.project2.remote.MovieService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val movieService: MovieService
) {
    suspend fun getPopularMovies(): PopularMoviesResponse {
        val response = movieService.getPopularMovies(language = "en-US", page = 1)
        if (response.isSuccessful) {
            response.body()?.let {
                return it
            } ?: throw Exception("Failed to load movies: Empty body")
        } else {
            throw Exception("Failed to load movies: ${response.errorBody()?.string()}")
        }
    }
}