package com.example.cis651syracuse.model

import com.example.cis651syracuse.util.GameDifficulty

data class PlayerState(
    val difficulty: GameDifficulty = GameDifficulty.UNKNOWN,
    val currentScore: Int = 0,
)