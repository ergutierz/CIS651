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

    suspend fun getPopularMovies(): List<Movie?> {
        return _moviesState.value.ifEmpty {
            withContext(Dispatchers.IO) {
                runCatching { movieService.getPopularMovies(language = "en-US", page = 1) }.fold(
                    onSuccess = { response ->
                        (response.body()?.results ?: emptyList()).also { results ->
                            _moviesState.update { results.mapNotNull { result -> result } }
                        }
                    },
                    onFailure = {
                        Log.e("MoviesRepository", "Failed to load movies", it)
                        emptyList()
                    }
                )
            }
        }
    }

    suspend fun getMovieDetail(movieId: Int): MovieDetailResponse? {
        return withContext(Dispatchers.IO) {
            runCatching { movieService.getMovieDetail(movieId, language = "en-US") }.fold(
                onSuccess = { response -> response.body() },
                onFailure = {
                    Log.e("MoviesRepository", "Failed to load movie detail", it)
                    null
                }
            )
        }
    }
}