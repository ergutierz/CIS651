package com.example.cis651syracuse.project2.view.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cis651syracuse.project2.model.Movie
import com.example.cis651syracuse.project2.view.MovieListAdapter
import com.example.cis651syracuse.project2.viewmodel.MovieListViewModel

@Composable
fun MovieListScreen(
    movies: List<Movie>,
    modifier: Modifier = Modifier,
    onAction: (action: MovieListViewModel.Action) -> Unit
) {
    Surface(color = Color.Black) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                RecyclerView(context).apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = MovieListAdapter(movies, onAction)
                    setAdapter(adapter)
                }
            })
    }
}