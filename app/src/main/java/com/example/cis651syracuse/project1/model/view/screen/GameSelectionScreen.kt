package com.example.cis651syracuse.project1.model.view.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cis651syracuse.R
import com.example.cis651syracuse.project1.model.util.GameDifficulty
import com.example.cis651syracuse.project1.model.viewmodel.GameSelectionViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameSelectionScreen(
    modifier: Modifier = Modifier,
    onAction: (action: GameSelectionViewModel.Action) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.theme_background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                color = Color.White,
                text = stringResource(id = R.string.title_activity_game),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .basicMarquee()
                    .padding(top = 32.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DifficultyButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.easy),
                    difficulty = GameDifficulty.EASY,
                    onAction = onAction
                )
                DifficultyButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.medium),
                    difficulty = GameDifficulty.MEDIUM,
                    onAction = onAction
                )
                DifficultyButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.difficult),
                    difficulty = GameDifficulty.DIFFICULT,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
private fun DifficultyButton(
    modifier: Modifier = Modifier,
    text: String,
    difficulty: GameDifficulty,
    onAction: (action: GameSelectionViewModel.Action) -> Unit
) {
    val gradient = Brush.linearGradient(
        colors = listOf(Color.Green, Color.Blue),
        start = Offset(0f, 0f),
        end = Offset(100f, 100f)
    )

    Button(
        onClick = { onAction(GameSelectionViewModel.Action.DifficultySelected(difficulty)) },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = modifier
            .padding(horizontal = 8.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(50))
            .background(gradient)
    ) {
        Text(text = text, color = Color.White)
    }
}

@Preview
@Composable
fun GameScreenSelectionPreview() {
    GameSelectionScreen{}
}
