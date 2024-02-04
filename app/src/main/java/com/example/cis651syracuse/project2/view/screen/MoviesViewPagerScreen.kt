package com.example.cis651syracuse.project2.view.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cis651syracuse.project2.model.Movie
import com.example.cis651syracuse.project2.view.PageFragment
import com.example.cis651syracuse.project2.view.components.PagerFragmentHost
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoviesViewPagerScreen(
    movies: List<Movie>
) {
    val pagerState = rememberPagerState(pageCount = { movies.size })
    val coroutineScope = rememberCoroutineScope()
    val tabs = movies.map { it.title ?: "Untitled" }

    Column {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color.Black,
            contentColor = Color(0xFFFFD700),
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    height = 3.dp,
                    color = Color(0xFFFFD700)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title, color = Color(0xFFFFD700)) },
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
            PagerFragmentHost(
                fragment = PageFragment.newInstance.apply { movie = movies[page] }
            )
        }
    }
}
