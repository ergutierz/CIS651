package com.example.cis651syracuse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameSetup()
        setContent {
            val state: GameViewModel.GameState by viewModel.state.collectAsState()
            GameScreen(
                modifier = Modifier
                    .fillMaxSize(),
                gameState = state,
                onAction = viewModel::onAction,
                exit = ::finish
            )
        }
    }

    private fun gameSetup() {
        val difficulty = intent.getIntExtra(EXTRA_GAME_LEVEL, GameDifficulty.EASY.ordinal)
        viewModel.onAction(GameViewModel.Action.InitializeGame(GameDifficulty.values()[difficulty]))
        lifecycleScope.launch {
            viewModel.event.onEach { event ->
                when (event) {
                    is GameViewModel.Event.NavigateToScoreDisplay -> {
                        startActivity(ScoreActivity.newIntent(this@GameActivity, event.score))
                        finish()
                    }
                }
            }.launchIn(this)

        }
    }
    companion object {
        private const val EXTRA_GAME_LEVEL = "com.example.cis651syracuse.GAME_LEVEL"

        fun newIntent(context: Context, gameDifficulty: GameDifficulty): Intent {
            return Intent(context, GameActivity::class.java).apply {
                putExtra(EXTRA_GAME_LEVEL, gameDifficulty.ordinal)
            }
        }
    }
}