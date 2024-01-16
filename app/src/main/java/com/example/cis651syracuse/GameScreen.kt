package com.example.cis651syracuse

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    gameState: GameViewModel.GameState,
    onAction: (action: GameViewModel.Action) -> Unit,
    exit: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            GameTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                exit = exit
            )

        },
        content = { padding ->
            GameGrid(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                gameState = gameState,
                onAction = onAction
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameTopAppBar(
    modifier: Modifier = Modifier,
    exit: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = stringResource(id = R.string.title_activity_game)) },
        navigationIcon = {
            IconButton(onClick = exit) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun GameGrid(
    modifier: Modifier = Modifier,
    gameState: GameViewModel.GameState,
    onAction: (action: GameViewModel.Action) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(gameState.columns),
        content = {
            itemsIndexed(gameState.grid) { index, item ->
                if (!item.isRemoved) {
                    val animatedRotation = animateFloatAsState(
                        targetValue = if (item.isFlipped) 0f else 180f,
                        animationSpec = tween(durationMillis = 300), label = ""
                    )
                    Card(
                        drawableId = item.imageRes,
                        onClick = {
                            onAction(GameViewModel.Action.FlipCard(index))
                        },
                        rotationDegrees = animatedRotation.value
                    )
                }
            }
        }
    )
}

@Composable
private fun Card(
    drawableId: Int,
    onClick: () -> Unit,
    rotationDegrees: Float
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.LightGray)
            .border(3.dp, Color.Black)
            .graphicsLayer {
                rotationY = rotationDegrees
                cameraDistance = 12f * density
            }
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (rotationDegrees < 90f) {
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = null
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            )
        }
    }
}