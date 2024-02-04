package com.example.cis651syracuse.project2.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cis651syracuse.project2.model.MovieDetailResponse
import com.example.cis651syracuse.project2.repository.MoviesRepository
import com.example.cis651syracuse.project2.util.DeviceUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    init {
        val isLargeScreen = DeviceUtils.isTablet(context)
        if (isLargeScreen) {
            viewModelScope.launch {
                moviesRepository.selectedMovieId.collect { movieId ->
                    if (movieId != -1) {
                        getMovieDetail()
                    }
                }
            }
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.GetMovieDetail -> getMovieDetail()
        }
    }

    private fun getMovieDetail() {
        setLoadingState(true)
        viewModelScope.launch {
            val response = moviesRepository.getMovieDetail()
            _viewState.update { oldState ->
                oldState.copy(
                    movieDetail = response,
                    displayDetail = response != null,
                    displayError = response == null,
                    isLoading = false
                )
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        _viewState.update { it.copy(isLoading = isLoading) }
    }

    data class ViewState(
        val movieDetail: MovieDetailResponse? = null,
        val displayError: Boolean = false,
        val displayDetail: Boolean = false,
        val isLoading: Boolean = false
    )

    sealed class Action {
        object GetMovieDetail: Action()
    }
}