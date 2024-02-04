package com.example.cis651syracuse.project2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.cis651syracuse.project2.model.Movie
import com.example.cis651syracuse.project2.view.components.MovieCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PageFragment : Fragment() {

    var movie: Movie? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                movie?.let {
                    MovieCard(movie = it)
                }
            }
        }
    }

    companion object {
        val newInstance get() = PageFragment()
    }
}