package com.example.cis651syracuse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    GameTopAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                },
                content = { padding ->
                    GameGrid(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    )
                }
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun GameTopAppBar(modifier: Modifier = Modifier) {
        TopAppBar(
            modifier = modifier,
            title = { Text(text = stringResource(id = R.string.title_activity_game)) },
            navigationIcon = {
                IconButton(onClick = ::finish) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )
    }

    @Composable
    private fun GameGrid(modifier: Modifier = Modifier) {
        val cards = listOf(
            R.drawable.img1, R.drawable.img2, R.drawable.img3,
            R.drawable.img4, R.drawable.img5, R.drawable.img6,
            R.drawable.img7, R.drawable.img8, R.drawable.img9,
            R.drawable.img10
        )
        val difficulty = intent.getIntExtra(EXTRA_GAME_LEVEL, GameLevel.EASY.ordinal)
        val (rows, cols) = when (difficulty) {
            GameLevel.MEDIUM.ordinal -> 4 to 4
            GameLevel.DIFFICULT.ordinal -> 5 to 5
            else -> 3 to 3
        }
        val drawableList = createDrawableList(rows * cols, cards)

        val cardFaceUpState = remember { mutableStateListOf<Boolean>().apply { addAll(List(drawableList.size) { false }) }}

        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(cols),
            content = {
                items(drawableList.size) { index ->
                    val isFaceUp = cardFaceUpState[index]
                    val animatedRotation = animateFloatAsState(
                        targetValue = if (isFaceUp) 0f else 180f,
                        animationSpec = tween(durationMillis = 300), label = ""
                    )
                    Card(
                        drawableId = drawableList[index],
                        onClick = { cardFaceUpState[index] = !isFaceUp },
                        rotationDegrees = animatedRotation.value
                    )
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


    private fun createDrawableList(size: Int, drawables: List<Int>): List<Int> {
        val numberOfUniqueCards = (size * 0.25).toInt()
        val uniqueCards = drawables.shuffled().take(numberOfUniqueCards)
        val remainingCards =
            (1..size - numberOfUniqueCards).flatMap { uniqueCards.shuffled().take(2) }
        return (uniqueCards + remainingCards).shuffled().take(size)
    }

    companion object {
        private const val EXTRA_GAME_LEVEL = "com.example.cis651syracuse.GAME_LEVEL"

        fun newIntent(context: Context, gameLevel: GameLevel): Intent {
            return Intent(context, GameActivity::class.java).apply {
                putExtra(EXTRA_GAME_LEVEL, gameLevel.ordinal)
            }
        }
    }
}