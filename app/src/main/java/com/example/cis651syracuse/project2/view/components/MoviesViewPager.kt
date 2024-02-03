package com.example.cis651syracuse.project2.view.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import com.example.cis651syracuse.project2.model.Movie
import com.example.cis651syracuse.project2.viewmodel.MovieListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoviesViewPager(
    movies: List<Movie>,
    onAction: (action: MovieListViewModel.Action) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = {movies.size})
    val coroutineScope = rememberCoroutineScope()
    val tabs = movies.map { it.title ?: "Untitled" }

    Column {
        // Adjust TabRow to have a black background and golden letters and indicator
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color.Black, // Black background
            contentColor = Color(0xFFFFD700), // Golden content
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    height = 3.dp,
                    color = Color(0xFFFFD700) // Golden indicator
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title, color = Color(0xFFFFD700)) }, // Golden text
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val pageOffset = remember { mutableStateOf(0f) }
            LaunchedEffect(pagerState.currentPageOffsetFraction) {
                pageOffset.value = pagerState.currentPageOffsetFraction
            }
            val animatedAlpha = animateFloatAsState(
                targetValue = 1f - pageOffset.value.absoluteValue, label = ""
            ).value

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = animatedAlpha
                    }
                    .fillMaxSize()
            ) {
                MovieCard(movie = movies[page], onAction = onAction)
            }
        }
    }
}
