package com.example.cis651syracuse.project2.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.cis651syracuse.project2.util.Screen

@Composable
fun ScreenContainer(
    modifier: Modifier = Modifier,
    screen: Screen,
    fragmentHost: @Composable () -> Unit,
    onCloseClick: () -> Unit = {},
    onNavItemClick: (screen: Screen) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        topBar = {
            TopNavBar(
                screen = screen,
                scaffoldState = scaffoldState,
                onCloseClick = onCloseClick
            )
        },
        drawerContent = {
            Surface(color = Color.Black) {
                NavigationDrawerContent(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                    onNavItemClick = onNavItemClick
                )
            }
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                fragmentHost()
            }
        }
    )
}