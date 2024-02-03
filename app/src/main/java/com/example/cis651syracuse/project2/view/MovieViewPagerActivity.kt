package com.example.cis651syracuse.project2.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.cis651syracuse.project2.util.NavUtil
import com.example.cis651syracuse.project2.util.Screen
import com.example.cis651syracuse.project2.view.components.ScreenContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieViewPagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenContainer(
                modifier = Modifier.fillMaxSize(),
                screen = Screen.MovieViewPager,
                fragment = ViewPagerFragment.newInstance,
                onCloseClick = ::finish,
                onNavItemClick = { screen ->
                    NavUtil.navigateTo(this, Screen.MovieViewPager, screen)?.let {
                        startActivity(it)
                    }
                }
            )
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MovieViewPagerActivity::class.java)
        }
    }
}