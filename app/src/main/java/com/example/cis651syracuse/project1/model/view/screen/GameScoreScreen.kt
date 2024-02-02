package com.example.cis651syracuse.project1.model.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cis651syracuse.R
import com.example.cis651syracuse.project1.model.viewmodel.GameScoreViewModel

@Composable
fun GameScoreScreen(
    modifier: Modifier = Modifier,
    scoreState: GameScoreViewModel.ScoreState,
    onAction: (action: GameScoreViewModel.Action) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            GameScoreTopAppBar(onAction = onAction)
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color.Black, Color.Blue)
                        )
                    )
            ) {
                Text(
                    color = Color.White,
                    text = stringResource(id = R.string.final_score, scoreState.currentScore),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(scoreState.scores) { score ->
                        ScoreItem(score)
                    }
                }
            }
        }
    )
}

@Composable
private fun ScoreItem(score: GameScoreViewModel.ScoreItem) {
    Text(
        color = Color.White,
        text = stringResource(id = R.string.score_history_item, score.score, score.formattedDate),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameScoreTopAppBar(onAction: (action: GameScoreViewModel.Action) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Black, Color.Blue)
                )
            )
    ) {
        TopAppBar(
            title = {
                Text(
                    color = Color.White,
                    text = stringResource(id = R.string.title_activity_score)
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    onAction(GameScoreViewModel.Action.OnExitClicked)
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}