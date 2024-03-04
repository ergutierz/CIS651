package com.example.cis651syracuse.project3.view

sealed class Screen(val route: String) {
    data object LoginScreen : Screen("login_screen")
    data object RegisterScreen : Screen("register_screen")
    data object ForgotPasswordScreen : Screen("forgot_password_screen")
    data object DashboardScreen : Screen("dashboard_screen")
    data object UserProfileScreen : Screen("user_profile_screen")
    data object PostCreationScreen : Screen("post_creation_screen")
    data object PostEditScreen : Screen("post_edit_screen")
    data object PostDetailScreen : Screen("post_detail_screen")

    companion object {
        fun getScreenTitle(route: String?): String {
            return when (route) {
                LoginScreen.route -> "Login"
                RegisterScreen.route -> "Register"
                ForgotPasswordScreen.route -> "Forgot Password"
                UserProfileScreen.route -> "User Profile"
                PostCreationScreen.route -> "Create Post"
                PostEditScreen.route -> "Edit Post"
                PostDetailScreen.route -> "Post Details"
                else -> "Poster APP"
            }
        }
    }
}
