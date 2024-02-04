package com.example.cis651syracuse.project2.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.cis651syracuse.project2.util.DeviceUtils
import com.example.cis651syracuse.project2.util.NavUtil
import com.example.cis651syracuse.project2.util.Screen
import com.example.cis651syracuse.project2.view.components.FragmentHost
import com.example.cis651syracuse.project2.view.components.ScreenContainer
import com.example.cis651syracuse.project2.viewmodel.MovieListDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListDetailActivity : AppCompatActivity() {

    private val viewModel: MovieListDetailViewModel by viewModels()
    private val isLargeDisplay: Boolean get() = DeviceUtils.isTablet(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewState: MovieListDetailViewModel.ViewState by viewModel.viewState.collectAsState()
            when {
                isLargeDisplay -> DisplayMasterDetail()
                viewState.isDetailVisible -> DisplayDetail()
                else -> DisplayList()
            }
        }
    }

    @Composable
    private fun DisplayList() {
        ScreenContainer(
            modifier = Modifier.fillMaxSize(),
            screen = Screen.Movies,
            fragment = ListFragment.newInstance,
            onCloseClick = ::finish,
            onNavItemClick = { screen ->
                NavUtil.navigateTo(this, Screen.Movies, screen)?.let {
                    startActivity(it)
                }
            }
        )
    }

    @Composable
    private fun DisplayDetail() {
        ScreenContainer(
            modifier = Modifier.fillMaxSize(),
            screen = Screen.MovieDetail,
            fragment = DetailFragment.newInstance,
            onCloseClick = {
                viewModel.onAction(MovieListDetailViewModel.Action.HideMovieDetail)
            },
            onNavItemClick = { screen ->
                NavUtil.navigateTo(this, Screen.MovieDetail, screen)?.let {
                    startActivity(it)
                }
            }
        )
    }

    @Composable
    private fun DisplayMasterDetail() {
        ScreenContainer(
            modifier = Modifier.fillMaxSize(),
            screen = Screen.MovieDetail,
            masterDetailHost = {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.weight(1f)) {
                        FragmentHost(fragment = ListFragment.newInstance)
                    }
                    Box(modifier = Modifier.weight(2f)) {
                        FragmentHost(fragment = DetailFragment.newInstance)
                    }
                }
            },
            onCloseClick = ::finish,
            onNavItemClick = { screen ->
                NavUtil.navigateTo(this, Screen.MovieDetail, screen)?.let {
                    startActivity(it)
                }
            }
        )
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MovieListDetailActivity::class.java)
        }
    }
}