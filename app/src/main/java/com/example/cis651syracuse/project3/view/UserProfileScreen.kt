package com.example.cis651syracuse.project3.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cis651syracuse.core.ErrorDialog
import com.example.cis651syracuse.core.InputField
import com.example.cis651syracuse.core.LoadingBar
import com.example.cis651syracuse.core.SuccessDialog
import com.example.cis651syracuse.project3.viewmodel.UserProfileViewModel
import com.example.cis651syracuse.core.handleEvent

@Composable
fun UserProfileScreen() {
    val viewModel: UserProfileViewModel = hiltViewModel()
    val viewState by viewModel.viewState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "User Profile", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))
        InputField(
            initialValue = viewState.displayName.orEmpty(),
            onValueChange = {
                viewModel.onAction(UserProfileViewModel.Action.UpdateDisplayName(it))
            },
            label = "Name",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputField(
            initialValue = viewState.phoneNumber.orEmpty(),
            onValueChange = {
                viewModel.onAction(UserProfileViewModel.Action.UpdatePhoneNumber(it))
            },
            label = "PhoneNumber",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.onAction(UserProfileViewModel.Action.UpdateUserProfile)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Profile")
        }

        ConsumeEvent(viewState)
        if (viewState.isLoading) LoadingBar()
    }
}

@Composable
private fun ConsumeEvent(viewState: UserProfileViewModel.ViewState) = with(viewState) {
    consumableEvent.handleEvent { event ->
        when (event) {
            is UserProfileViewModel.Event.Error -> ErrorDialog()
            is UserProfileViewModel.Event.Success -> SuccessDialog()
        }
    }
}
