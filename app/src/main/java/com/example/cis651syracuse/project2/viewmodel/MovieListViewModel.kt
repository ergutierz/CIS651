package com.example.cis651syracuse.project2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cis651syracuse.project2.model.Movie
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

    fun onAction(action: Action) {
        when (action) {
            is Action.DisplayMovieDetail -> displayMovieDetail(action.movieId)
            is Action.GetPopularMovies -> getPopularMovies()
        }
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
        moviesRepository.selectMovie(movieId)
        moviesRepository.displayDetail()
    }

    data class ViewState(
        val movies: List<Movie> = emptyList(),
        val isLoading: Boolean = false,
        val displayError: Boolean = false,
        val displayList: Boolean = false
    )

    sealed interface Action {
        data class DisplayMovieDetail(val movieId: Int) : Action
        object GetPopularMovies : Action
    }
}