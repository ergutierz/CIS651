package com.example.cis651syracuse.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.cis651syracuse.view.screen.GameSelectionScreen
import com.example.cis651syracuse.viewmodel.GameSelectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class GameSelectionActivity : AppCompatActivity() {

    private val viewModel: GameSelectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameSelectionScreen(
                modifier = Modifier.fillMaxSize(),
                onAction = viewModel::onAction
            )
            LaunchedEffect("event") {
                viewModel.event.onEach { event ->
                    when (event) {
                        is GameSelectionViewModel.Event.NavigateToGame -> {
                            startActivity(
                                GameActivity.newIntent(this@GameSelectionActivity)
                            )
                        }
                    }
                }.launchIn(this)
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, GameSelectionActivity::class.java)
        }
    }
}