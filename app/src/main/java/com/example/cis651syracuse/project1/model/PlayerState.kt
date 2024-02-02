package com.example.cis651syracuse.project1.model

import com.example.cis651syracuse.project1.model.util.GameDifficulty

data class PlayerState(
    val difficulty: GameDifficulty = GameDifficulty.UNKNOWN,
    val currentScore: Int = 0,
)