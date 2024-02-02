package com.example.cis651syracuse.project2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cis651syracuse.project2.repository.AuthenticationRepository
import com.example.cis651syracuse.project2.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    init {
        authenticate()
    }

    private fun authenticate() {
        viewModelScope.launch {
            val response = authenticationRepository.authenticate()
            if (response.success == true) {
                fetchMovies()
            }
        }
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            val response = moviesRepository.getPopularMovies()
        }
    }
}