package com.example.cis651syracuse.project3.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cis651syracuse.core.*
import com.example.cis651syracuse.project3.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen() {
    val viewModel: RegisterViewModel = hiltViewModel()
    val viewState: RegisterViewModel.ViewState by viewModel.viewState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        InputField(onValueChange = {
            viewModel.onAction(RegisterViewModel.Action.UpdateEmail(it))
        }, label = "Email", modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))

        InputField(onValueChange = {
            viewModel.onAction(RegisterViewModel.Action.UpdatePassword(it))
        }, label = "Password", modifier = Modifier.fillMaxWidth(), isPassword = true)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = viewState.isFormValid,
            onClick = {
                viewModel.onAction(RegisterViewModel.Action.Register)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { viewModel.onAction(RegisterViewModel.Action.NavigateToLogin) }) {
            Text("Already have an account? Log in", fontWeight = FontWeight.Bold)
        }
    }
    ConsumeEvent(viewState)
    if (viewState.isLoading) LoadingBar()
}

@Composable
private fun ConsumeEvent(viewState: RegisterViewModel.ViewState) = with(viewState) {
    consumableEvent.handleEvent { event ->
        when (event) {
            is RegisterViewModel.Event.Error -> ErrorDialog()
        }
    }
}
