package com.example.cis651syracuse.project2.view.components

import androidx.compose.foundation.clickable
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.cis651syracuse.project2.util.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(
    screen: Screen,
    scaffoldState: ScaffoldState,
    onCloseClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = screen.title,
                style = MaterialTheme.typography.headlineMedium.copy(color = Color(0xFFFFD700)), // Updated for cinema theme
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier.clickable {
                    if (!screen.isDashboard) {
                        onCloseClick()
                    } else coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                imageVector = if (screen.isDashboard) Icons.Filled.Menu else Icons.Filled.ArrowBack,
                contentDescription = if (screen.isDashboard) "Menu" else "Close",
                tint = Color(0xFFFFD700)
            )
        },
        colors = centerAlignedTopAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color(0xFFFFD700),
            navigationIconContentColor = Color(0xFFFFD700)
        )
    )
}
