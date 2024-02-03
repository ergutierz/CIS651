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
class MovieViewPagerViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> get() = _viewState.asStateFlow()

    fun onAction(action: Action) {
        when (action) {
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
                    displayCarousel = movies.isNotEmpty(),
                    isLoading = false
                )
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        _viewState.update { it.copy(isLoading = isLoading) }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val displayCarousel: Boolean = false,
        val displayError: Boolean = false
    )

    sealed interface Action {
        object GetPopularMovies : Action
    }
}