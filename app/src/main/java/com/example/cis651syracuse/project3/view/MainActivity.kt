package com.example.cis651syracuse.project3.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.cis651syracuse.project3.util.NavigationCommandManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigationCommandManager: NavigationCommandManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MainContainer(
                modifier = Modifier.fillMaxSize(),
                navController = navController
            )

            LaunchedEffect("navigation") {
                navigationCommandManager.commands.onEach { navigationCommand ->
                    if (navigationCommand.destination.isNotEmpty()) {
                        navController.navigate(navigationCommand.destination)
                    }
                }.launchIn(this)
            }
        }
    }
}