package com.example.cis651syracuse.project1.model.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cis651syracuse.project1.model.repository.GameRepository
import com.example.cis651syracuse.project1.model.util.GameDifficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameSelectionViewModel @Inject constructor(
    private val gameRepository: GameRepository
): ViewModel() {

    private val _event = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> get() = _event.asSharedFlow()

    fun onAction(action: Action) {
        when (action) {
            is Action.DifficultySelected -> difficultySelected(action.difficulty)
        }
    }

    private fun difficultySelected(difficulty: GameDifficulty) {
        gameRepository.updatePlayerDifficulty(difficulty)
        viewModelScope.launch { _event.emit(Event.NavigateToGame) }
    }

    sealed interface Action {
        data class DifficultySelected(val difficulty: GameDifficulty) : Action
    }

    sealed interface Event {
        object NavigateToGame : Event
    }
}