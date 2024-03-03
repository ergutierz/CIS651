package com.example.cis651syracuse.project3.util

import androidx.navigation.NamedNavArgument
import com.example.cis651syracuse.core.ApplicationScope
import com.example.cis651syracuse.project3.view.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationCommandManager @Inject constructor(
    @ApplicationScope private val coroutineScope: CoroutineScope,
) : CoroutineScope by coroutineScope {

    private val commandStore: MutableSharedFlow<NavigationCommand> = MutableSharedFlow(replay = 1)
    val commands: SharedFlow<NavigationCommand> = commandStore.asSharedFlow()

    fun navigate(directions: NavigationCommand) {
        launch { commandStore.emit(directions) }
    }

    fun logout() {
        launch { commandStore.emit(defaultDirection) }
    }

    companion object {
        val defaultDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = ""
        }

        val loginDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.LoginScreen.route
        }

        val registerDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.RegisterScreen.route
        }

        val forgotPasswordDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.ForgotPasswordScreen.route
        }

        val dashboardDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = Screen.DashboardScreen.route
        }
    }
}
