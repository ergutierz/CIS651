package com.example.cis651syracuse.project1.model.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.cis651syracuse.project1.model.view.screen.GameScoreScreen
import com.example.cis651syracuse.project1.model.viewmodel.GameScoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class GameScoreActivity : AppCompatActivity() {

    private val viewModel: GameScoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scoreState: GameScoreViewModel.ScoreState by viewModel.state.collectAsState()
            GameScoreScreen(
                scoreState = scoreState,
                onAction = viewModel::onAction
            )

            LaunchedEffect("event") {
                viewModel.event.onEach { event ->
                    when (event) {
                        is GameScoreViewModel.Event.NavigateToGameSelection -> {
                            startActivity(GameSelectionActivity.newIntent(this@GameScoreActivity))
                            finish()
                        }
                    }
                }.launchIn(this)
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, GameScoreActivity::class.java)
        }
    }
}