package com.example.cis651syracuse.project3.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.material.ScaffoldState
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun NavDrawer(
    modifier: Modifier = Modifier,
    navController: NavController,
    scaffoldState: ScaffoldState
) {
    Column(modifier = modifier) {
        Text(text = "Poster APP", style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))
        Divider()
        DrawerItem("Login", Screen.LoginScreen.route, navController, scaffoldState)
        DrawerItem("Register", Screen.RegisterScreen.route, navController, scaffoldState)
        DrawerItem("User Profile", Screen.UserProfileScreen.route, navController, scaffoldState)
        DrawerItem("Create Post", Screen.PostCreationScreen.route, navController, scaffoldState)
        DrawerItem("Feed", Screen.PostFeedScreen.route, navController, scaffoldState)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerItem(text: String, route: String, navController: NavController, scaffoldState: ScaffoldState) {
    val coroutineScope = rememberCoroutineScope()
    ListItem(
        text = { Text(text) },
        modifier = Modifier.clickable {
            coroutineScope.launch {
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
                scaffoldState.drawerState.close()
            }
        }
    )
}
