package com.example.cis651syracuse.project2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cis651syracuse.project2.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    fun onAction(action: Action) {
        when (action) {
            is Action.Authenticate -> authenticate()
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            authenticationRepository.authenticate()
        }
    }

    sealed interface Action {
        object Authenticate : Action
    }
}