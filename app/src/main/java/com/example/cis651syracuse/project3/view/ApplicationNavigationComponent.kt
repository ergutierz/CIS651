package com.example.cis651syracuse.project3.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cis651syracuse.project3.view.DashboardScreen
import com.example.cis651syracuse.project3.view.LoginScreen
import com.example.cis651syracuse.project3.view.Screen

@Composable
fun ApplicationNavigationComponent(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {
        composable(Screen.LoginScreen.route) { LoginScreen() }
        composable(Screen.DashboardScreen.route) { DashboardScreen() }
        composable(Screen.PostCreationScreen.route) { PostCreationScreen() }
        composable(Screen.PostFeedScreen.route) { PostFeedScreen() }
        composable(Screen.RegisterScreen.route) { RegisterScreen() }
        composable(Screen.UserProfileScreen.route) { UserProfileScreen() }
    }
}