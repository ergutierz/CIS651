package com.example.cis651syracuse.project3.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cis651syracuse.core.ErrorDialog
import com.example.cis651syracuse.core.InputField
import com.example.cis651syracuse.core.LoadingBar
import com.example.cis651syracuse.core.handleEvent
import com.example.cis651syracuse.project3.viewmodel.NavDrawerViewModel
import kotlinx.coroutines.launch

@Composable
fun NavDrawer(
    modifier: Modifier = Modifier,
    navController: NavController,
    scaffoldState: ScaffoldState
) {
    val viewModel: NavDrawerViewModel = hiltViewModel()
    val viewState: NavDrawerViewModel.ViewState by viewModel.viewState.collectAsState()
    Column(modifier = modifier) {
        OutlinedButton(
            onClick = {
                viewModel.onAction(NavDrawerViewModel.Action.DisplayLoginDialog)
            },
            shape = CircleShape,
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp),
            border = BorderStroke(1.dp, Color.Blue),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Blue)
        ) {
            Text("USER", color = Color.White)
        }

        Divider()
        DrawerItem("User Profile", Screen.UserProfileScreen.route, navController, scaffoldState)
        DrawerItem("Create Post", Screen.PostCreationScreen.route, navController, scaffoldState)
        DrawerItem("Feed", Screen.PostFeedScreen.route, navController, scaffoldState)
        DrawerItem("Sign Out", Screen.LoginScreen.route, navController, scaffoldState)
    }
    ConsumeEvent(viewState)
    if (viewState.isLoading) LoadingBar()
    if (viewState.isLoginDialogVisible) UserSwitchDialog(viewState, viewModel::onAction)
}

@Composable
private fun ConsumeEvent(
    viewState: NavDrawerViewModel.ViewState
) = with(viewState) {
    consumableEvent.handleEvent { event ->
        when (event) {
            is NavDrawerViewModel.Event.Error -> ErrorDialog()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerItem(
    text: String,
    route: String,
    navController: NavController,
    scaffoldState: ScaffoldState
) {
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

@Composable
private fun UserSwitchDialog(
    viewState: NavDrawerViewModel.ViewState,
    onAction: (NavDrawerViewModel.Action) -> Unit
) {

    AlertDialog(
        onDismissRequest = {
            onAction(NavDrawerViewModel.Action.DismissLoginDialog)
        },
        title = {
                Column {
                    Text(text = "Current User: ${viewState.currentUserName}")
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(text = "Switch User")
                }
        },
        text = {
            Column {
                InputField(onValueChange = {
                    onAction(NavDrawerViewModel.Action.UpdateEmail(it))
                }, label = "Email", modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(8.dp))

                InputField(onValueChange = {
                    onAction(NavDrawerViewModel.Action.UpdatePassword(it))
                }, label = "Password", modifier = Modifier.fillMaxWidth(), isPassword = true)
            }
        },
        confirmButton = {
            Column {
                TextButton(
                    onClick = {
                        onAction(NavDrawerViewModel.Action.DismissLoginDialog)
                    }
                ) {
                    Text("LOGIN")
                }
                TextButton(
                    onClick = {
                        onAction(NavDrawerViewModel.Action.DismissLoginDialog)
                    }
                ) {
                    Text("EXIT")
                }
            }
        }
    )
}
