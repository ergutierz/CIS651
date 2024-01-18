package com.example.cis651syracuse.repository

import com.example.cis651syracuse.model.GameScore
import com.example.cis651syracuse.model.PlayerState
import com.example.cis651syracuse.util.GameDifficulty
import com.example.cis651syracuse.util.SharedPreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val sharedPrefsManager: SharedPreferencesManager
) {

    private val _playerState: MutableStateFlow<PlayerState> = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> get() = _playerState.asStateFlow()

    val gameScores: Flow<List<GameScore>>
        get() = sharedPrefsManager.gameScores

    private suspend fun saveGameScoreWithTimestamp(score: Int) {
        val currentScores = gameScores.first().toMutableList()
        currentScores.add(GameScore(score, System.currentTimeMillis()))
        sharedPrefsManager.saveGameScores(currentScores)
    }

    suspend fun updatePlayerCurrentScore(score: Int) {
        _playerState.update { oldState ->
            oldState.copy(currentScore = score)
        }
        saveGameScoreWithTimestamp(score)
    }

    fun updatePlayerDifficulty(difficulty: GameDifficulty) {
        _playerState.update { oldState ->
            oldState.copy(difficulty = difficulty)
        }
    }
}