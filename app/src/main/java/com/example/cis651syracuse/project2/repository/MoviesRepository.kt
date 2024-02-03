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

    private val _selectedMovies = MutableStateFlow<List<MovieDetailResponse>>(emptyList())
    private val _moviesState = MutableStateFlow<List<Movie>>(emptyList())
    private val _selectedMovieId = MutableStateFlow(-1)
    val selectedMovieId: StateFlow<Int> get() = _selectedMovieId.asStateFlow()
    private val _isDetailVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isDetailVisible: StateFlow<Boolean> get() = _isDetailVisible.asStateFlow()

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

    suspend fun getMovieDetail(): MovieDetailResponse? {
        return _selectedMovies.value.find { cachedSelectedMovie -> cachedSelectedMovie.id == _selectedMovieId.value }
            ?: withContext(Dispatchers.IO) {
                runCatching {
                    movieService.getMovieDetail(_selectedMovieId.value, language = "en-US")
                        .also { response ->
                            _selectedMovies.update {
                                it.toMutableList()
                                    .apply { response.body()?.let { movie -> add(movie) } }
                            }
                        }
                }.fold(
                    onSuccess = { response -> response.body() },
                    onFailure = {
                        Log.e("MoviesRepository", "Failed to load movie detail", it)
                        null
                    }
                )
            }
    }

    fun selectMovie(movieId: Int) {
        _selectedMovieId.update { movieId }
    }

    fun displayDetail() {
        _isDetailVisible.update { true }
    }

    fun hideDetail() {
        _isDetailVisible.update { false }
    }
}