package com.example.cis651syracuse.project1.model.view.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cis651syracuse.project1.model.viewmodel.GameViewModel
import com.example.cis651syracuse.R

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    gameState: GameViewModel.GameState,
    onAction: (action: GameViewModel.Action) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            GameTopAppBar(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onAction = onAction
            )

        },
        content = { padding ->
            val composition by rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(R.raw.rotating_circle)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color.Black, Color.Blue)
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GameGrid(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp),
                    gameState = gameState,
                    onAction = onAction
                )
                LottieAnimation(
                    composition = composition,
                    isPlaying = true,
                    modifier = Modifier.size(200.dp).weight(1f),
                    iterations = LottieConstants.IterateForever
                )
            }

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameTopAppBar(
    modifier: Modifier = Modifier,
    onAction: (action: GameViewModel.Action) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Black, Color.Blue)
                )
            )
    ) {
        TopAppBar(
            title = { Text(color = Color.White, text = stringResource(id = R.string.title_activity_deck)) },
            navigationIcon = {
                IconButton(onClick = {
                    onAction(GameViewModel.Action.OnBackClicked)
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
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
        Image(
            painter = painterResource(
                id = drawableId.takeIf { rotationDegrees < 90f } ?: R.drawable.blue_stargate
            ),
            contentDescription = null
        )
    }
}