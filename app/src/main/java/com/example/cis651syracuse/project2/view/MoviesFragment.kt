package com.example.cis651syracuse.project2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import com.example.cis651syracuse.project2.model.Movie
import com.example.cis651syracuse.project2.view.components.DisplayDetailScreenError
import com.example.cis651syracuse.project2.view.components.MoviesViewPager
import com.example.cis651syracuse.project2.view.components.RecyclerViewAdapterComponent
import com.example.cis651syracuse.project2.view.screen.MovieDetailScreen
import com.example.cis651syracuse.project2.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel: MovieListViewModel by lazy {
        ViewModelProvider(this)[MovieListViewModel::class.java]
    }

    var isListScreen = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val viewState: MovieListViewModel.ViewState by viewModel.viewState.collectAsState()
                when {
                    viewState.isLoading -> LoadingBar()
                    viewState.displayError -> DisplayDetailScreenError()
                    viewState.displayList -> MoviesViewPager(movies = viewState.movies, onAction = viewModel::onAction)//RecyclerViewAdapterComponent(movies = viewState.movies, onAction = viewModel::onAction).also { isListScreen = true }
                    else -> MovieDetailScreen(movieDetail = viewState.movieDetail).also { isListScreen = false }
                }
            }
        }
    }

    fun onCloseDetailListener() {
        viewModel.onAction(MovieListViewModel.Action.DisplayList)
    }

    @Composable
    private fun LoadingBar() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = Color(0xFFFFD700))
        }
    }

    private fun onMovieClick(movie: Movie) {
        // Handle movie click, navigate to details
        if (isLargeScreen()) {
            // Load detail fragment side by side
        } else {
            // Replace fragment or navigate to another activity
        }
    }

    private fun isLargeScreen(): Boolean {
        // Determine if the device is large enough for side-by-side display
        return resources.configuration.screenWidthDp >= 600
    }

    override fun onResume() {
        super.onResume()
        viewModel.onAction(MovieListViewModel.Action.GetPopularMovies)
    }

    companion object {
        val newInstance get() = MoviesFragment()
    }
}
