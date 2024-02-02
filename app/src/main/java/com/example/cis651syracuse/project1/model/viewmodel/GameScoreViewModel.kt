package com.example.cis651syracuse.project1.model.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cis651syracuse.project1.model.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GameScoreViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScoreState())
    val state: StateFlow<ScoreState> get() = _state.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> get() = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            combine(
                gameRepository.gameScores,
                gameRepository.playerState
            ) { scores, playerState ->
                ScoreState(scores.map { ScoreItem(it.score, it.timestamp) }, playerState.currentScore)
            }.collect { newState ->
                _state.update { oldState ->
                    oldState.copy(
                        scores = newState.scores,
                        currentScore = newState.currentScore
                    )
                }
            }
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.OnExitClicked -> navigateToGameSelection()
        }
    }

    private fun navigateToGameSelection() {
        viewModelScope.launch { _event.emit(Event.NavigateToGameSelection) }
    }

    data class ScoreState(
        val scores: List<ScoreItem> = emptyList(),
        val currentScore: Int = 0
    )

    data class ScoreItem(
        val score: Int,
        val timestamp: Long
    ) {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val formattedDate: String = dateFormat.format(Date(timestamp))
    }

    sealed interface Action {
        object OnExitClicked : Action
    }

    sealed interface Event {
        object NavigateToGameSelection : Event
    }
}