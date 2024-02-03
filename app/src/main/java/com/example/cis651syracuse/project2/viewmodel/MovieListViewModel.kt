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
            moviesRepository.moviesState.collect { movies ->
                _viewState.update { it.copy(movies = movies) }
            }
        }
        viewModelScope.launch {
            moviesRepository.movieDetailState.collect { movieDetail ->
                _viewState.update { oldState ->
                    val detail = movieDetail.find { it.id == oldState.selectedMovieId }
                    oldState.copy(
                        movieDetail = detail ?: oldState.movieDetail,
                        shouldDisplayList = detail != null
                    )
                }
            }
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.NavigateToDetail -> navigateToDetail(action.movieId)
        }
    }

    private fun navigateToDetail(movieId: Int) {
        _viewState.update { it.copy(selectedMovieId = movieId)}
        viewModelScope.launch {
            moviesRepository.getMovieDetail(movieId)
        }
    }

    data class ViewState(
        val movies: List<Movie> = emptyList(),
        val movieDetail: MovieDetailResponse = MovieDetailResponse(),
        val selectedMovieId: Int = -1,
        val shouldDisplayList: Boolean = true
    )

    sealed interface Action {
        data class NavigateToDetail(val movieId: Int) : Action
    }
}