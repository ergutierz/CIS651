package com.example.cis651syracuse.project2.repository

import android.util.Log
import com.example.cis651syracuse.project2.model.Movie
import com.example.cis651syracuse.project2.model.MovieDetailResponse
import com.example.cis651syracuse.project2.remote.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val movieService: MovieService
) {

    private val _moviesState = MutableStateFlow<List<Movie>>(emptyList())
    val moviesState: StateFlow<List<Movie>> = _moviesState.asStateFlow()

    private val _movieDetailState = MutableStateFlow<Set<MovieDetailResponse>>(setOf())
    val movieDetailState: StateFlow<Set<MovieDetailResponse>> = _movieDetailState.asStateFlow()

    suspend fun getPopularMovies() {
        if (_moviesState.value.isNotEmpty()) return
        withContext(Dispatchers.IO) {
            runCatching { movieService.getPopularMovies(language = "en-US", page = 1) }.fold(
                onSuccess = { response ->
                    response.body()?.results?.let { results ->
                        _moviesState.update { results.mapNotNull { movie -> movie } }
                    }
                },
                onFailure = {
                    Log.e("MoviesRepository", "Failed to load movies", it)
                }
            )
        }
    }

    suspend fun getMovieDetail(movieId: Int) {
        if (_movieDetailState.value.any { it.id == movieId }) return
        withContext(Dispatchers.IO) {
            runCatching { movieService.getMovieDetail(movieId, language = "en-US") }.fold(
                onSuccess = { response ->
                    response.body()?.let { movieDetail ->
                        _movieDetailState.update { it.toMutableSet().apply { add(movieDetail) } }
                    }
                },
                onFailure = {
                    Log.e("MoviesRepository", "Failed to load movie detail", it)
                }
            )
        }
    }
}