package com.example.cis651syracuse.project2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cis651syracuse.project2.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

@HiltViewModel
class MovieListDetailViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel(){

    val viewState: StateFlow<ViewState> get() = moviesRepository.isDetailVisible
        .transform { isDetailVisible -> emit(ViewState(isDetailVisible)) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ViewState()
    )

    fun onAction(action: Action) {
        when (action) {
            is Action.HideMovieDetail -> hideMovieDetail()
        }
    }

    private fun hideMovieDetail() {
        moviesRepository.hideDetail()
    }

    data class ViewState(
        val isDetailVisible: Boolean = false
    )

    sealed interface Action {
        object HideMovieDetail : Action
    }
}