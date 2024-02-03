package com.example.cis651syracuse.project2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import com.example.cis651syracuse.project2.view.components.ErrorScreen
import com.example.cis651syracuse.project2.view.components.LoadingBar
import com.example.cis651syracuse.project2.view.screen.MovieListScreen
import com.example.cis651syracuse.project2.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private val viewModel: MovieListViewModel by lazy {
        ViewModelProvider(this)[MovieListViewModel::class.java]
    }

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
                    viewState.displayError -> ErrorScreen()
                    viewState.displayList -> MovieListScreen(movies = viewState.movies, onAction = viewModel::onAction)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onAction(MovieListViewModel.Action.GetPopularMovies)
    }

    companion object {
        val newInstance get() = ListFragment()
    }
}
