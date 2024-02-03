package com.example.cis651syracuse.project2.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import com.example.cis651syracuse.project2.util.NavUtil
import com.example.cis651syracuse.project2.util.Screen
import com.example.cis651syracuse.project2.view.components.FragmentHost
import com.example.cis651syracuse.project2.view.components.ScreenContainer
import com.example.cis651syracuse.project2.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenContainer(
                modifier = Modifier.fillMaxSize(),
                screen = Screen.Dashboard,
                drawerGesturesEnabled = true,
                fragmentHost = {
                    FragmentHost(fragment = AboutFragment.newInstance)
                },
                onNavItemClick = { screen ->
                    NavUtil.navigateTo(this, Screen.Dashboard, screen)?.let {
                        startActivity(it)
                    }
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onAction(DashboardViewModel.Action.Authenticate)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, DashboardActivity::class.java)
        }
    }
}