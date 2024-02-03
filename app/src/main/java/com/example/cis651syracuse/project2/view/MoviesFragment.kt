package com.example.cis651syracuse.project2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.example.cis651syracuse.project2.model.Movie
import com.example.cis651syracuse.project2.repository.MoviesRepository
import com.example.cis651syracuse.project2.view.components.RecyclerViewAdapterComponent
import com.example.cis651syracuse.project2.view.screen.MovieDetailScreen
import com.example.cis651syracuse.project2.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel: MovieListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val viewState: MovieListViewModel.ViewState by viewModel.viewState.collectAsState()
                if (viewState.shouldDisplayList) RecyclerViewAdapterComponent(movies = viewState.movies, onAction = viewModel::onAction)
                else MovieDetailScreen(movieDetail = viewState.movieDetail)
            }
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

    companion object {
        val newInstance get() = MoviesFragment()
    }
}
