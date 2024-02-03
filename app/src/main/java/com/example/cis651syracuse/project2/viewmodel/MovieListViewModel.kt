package com.example.cis651syracuse.project2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cis651syracuse.project2.model.Movie
import com.example.cis651syracuse.project2.model.MovieDetailResponse
import com.example.cis651syracuse.project2.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            moviesRepository.getPopularMovies()
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.DisplayMovieDetail -> displayMovieDetail(action.movieId)
            is Action.GetPopularMovies -> getPopularMovies()
            is Action.DisplayList -> displayList()
        }
    }

    private fun displayList() {
        _viewState.update { it.copy(displayList = true) }
    }

    private fun getPopularMovies() {
        setLoadingState(true)
        viewModelScope.launch {
            val response = moviesRepository.getPopularMovies()
            val movies = response.mapNotNull { it }
            _viewState.update { oldState ->
                oldState.copy(
                    movies = movies,
                    displayError = movies.isEmpty(),
                    displayList = movies.isNotEmpty(),
                    isLoading = false
                )
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        _viewState.update { it.copy(isLoading = isLoading) }
    }

    private fun displayMovieDetail(movieId: Int) {
        setLoadingState(true)
        viewModelScope.launch {
            val response = moviesRepository.getMovieDetail(movieId)
            _viewState.update { oldState ->
                oldState.copy(
                    movieDetail = response,
                    displayError = response == null,
                    displayList = false,
                    isLoading = false
                )
            }
        }
    }

    data class ViewState(
        val movies: List<Movie> = emptyList(),
        val movieDetail: MovieDetailResponse? = null,
        val isLoading: Boolean = false,
        val displayError: Boolean = false,
        val displayList: Boolean = false
    )

    sealed interface Action {
        data class DisplayMovieDetail(val movieId: Int) : Action
        object GetPopularMovies : Action
        object DisplayList : Action
    }
}