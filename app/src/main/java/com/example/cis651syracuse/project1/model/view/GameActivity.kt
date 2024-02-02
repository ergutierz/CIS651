package com.example.cis651syracuse.project1.model.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.cis651syracuse.project1.model.view.screen.GameScreen
import com.example.cis651syracuse.project1.model.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class GameActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state: GameViewModel.GameState by viewModel.state.collectAsState()
            GameScreen(
                modifier = Modifier
                    .fillMaxSize(),
                gameState = state,
                onAction = viewModel::onAction
            )
            LaunchedEffect("events") {
                viewModel.event.onEach { event ->
                    when (event) {
                        is GameViewModel.Event.NavigateToScoreDisplay -> {
                            startActivity(GameScoreActivity.newIntent(this@GameActivity))
                            finish()
                        }
                        is GameViewModel.Event.NavigateToGameSelection -> {
                            startActivity(GameSelectionActivity.newIntent(this@GameActivity))
                            finish()
                        }
                    }
                }.launchIn(this)
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, GameActivity::class.java)
        }
    }
}