package com.example.cis651syracuse.project2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cis651syracuse.project2.view.components.ErrorScreen
import com.example.cis651syracuse.project2.view.components.LoadingBar
import com.example.cis651syracuse.project2.view.screen.MoviesViewPagerScreen
import com.example.cis651syracuse.project2.viewmodel.MovieViewPagerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewPagerFragment : Fragment() {

    private val viewModel: MovieViewPagerViewModel by lazy {
        ViewModelProvider(this)[MovieViewPagerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val viewState: MovieViewPagerViewModel.ViewState by viewModel.viewState.collectAsState()
                when {
                    viewState.isLoading -> LoadingBar()
                    viewState.displayError -> ErrorScreen()
                    viewState.displayCarousel -> MoviesViewPagerScreen(movies = viewState.movies)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onAction(MovieViewPagerViewModel.Action.GetPopularMovies)
    }

    companion object {
        val newInstance get() = ViewPagerFragment()
    }
}