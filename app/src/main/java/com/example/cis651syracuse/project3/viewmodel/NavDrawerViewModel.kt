package com.example.cis651syracuse.project3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cis651syracuse.core.ConsumableEvent
import com.example.cis651syracuse.core.ModelStore
import com.example.cis651syracuse.core.StateFlowModelStore
import com.example.cis651syracuse.project3.repository.AuthenticationRepository
import com.example.cis651syracuse.project3.util.FirebaseAuthenticationManager
import com.example.cis651syracuse.project3.util.NavigationCommandManager
import com.example.cis651syracuse.project3.util.UserManager
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavDrawerViewModel @Inject constructor(
    private val userManager: UserManager,
    private val authRepository: AuthenticationRepository,
    private val navigationCommandManager: NavigationCommandManager,
    private val firebaseAuthenticationManager: FirebaseAuthenticationManager
) : ViewModel() {
    private val _modelStore: ModelStore<ViewState> =
        StateFlowModelStore(ViewState(), viewModelScope)
    val viewState: StateFlow<ViewState>
        get() = _modelStore.modelState().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ViewState()
        )

    init {
        fetchUserProfile()
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.Login -> performLogin()
            is Action.UpdateEmail -> updateEmail(action.email)
            is Action.UpdatePassword -> updatePassword(action.password)
            is Action.DisplayLoginDialog -> displayLoginDialog()
            is Action.DismissLoginDialog -> dismissLoginDialog()
        }
    }

    private fun fetchUserProfile() {
        val userId = firebaseAuthenticationManager.getCurrentUser?.uid.orEmpty()
        userManager.getUser(userId) { _, user ->
            viewModelScope.launch {
                _modelStore.process { oldState ->
                    oldState.copy(
                        currentUserName = user?.displayName
                    )
                }
            }
        }
    }

    private fun dismissLoginDialog() {
        viewModelScope.launch {
            _modelStore.process { oldState ->
                oldState.copy(
                    isLoginDialogVisible = false
                )
            }
        }
    }

    private fun displayLoginDialog() {
        viewModelScope.launch {
            _modelStore.process { oldState ->
                oldState.copy(
                    isLoginDialogVisible = true
                )
            }
        }
    }

    private fun updateEmail(email: String) {
        viewModelScope.launch {
            _modelStore.process { oldState ->
                oldState.copy(
                    email = email,
                    isFormValid = email.isNotEmpty() && oldState.password.isNotEmpty()
                )
            }
        }
    }

    private fun updatePassword(password: String) {
        viewModelScope.launch {
            _modelStore.process { oldState ->
                oldState.copy(
                    password = password,
                    isFormValid = password.isNotEmpty() && oldState.email.isNotEmpty()
                )
            }
        }
    }

    private fun performLogin() {
        setLoadingState(true)
        val email = _modelStore.value.email
        val password = _modelStore.value.password
        authRepository.login(email, password) { firebaseUser: FirebaseUser?, exception: Exception? ->
            setLoadingState(false)
            if (exception == null && firebaseUser != null) {
                navigateToDashboard()
            } else {
                viewModelScope.launch {
                    _modelStore.process { oldState ->
                        oldState.copy(
                            consumableEvent = ConsumableEvent.create(
                                Event.Error(exception?.message ?: "Unknown error")
                            )
                        )
                    }
                }
            }
        }
    }

    private fun navigateToDashboard() {
        navigationCommandManager.navigate(NavigationCommandManager.dashboardDirection)
    }

    private fun setLoadingState(isLoading: Boolean) {
        viewModelScope.launch {
            _modelStore.process { oldState -> oldState.copy(isLoading = isLoading) }
        }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val email: String = "",
        val password: String = "",
        val isFormValid: Boolean = false,
        val currentUserName: String? = null,
        val isLoginDialogVisible: Boolean = false,
        val consumableEvent: ConsumableEvent<Event> = ConsumableEvent(),
    )

    sealed interface Action {
        data class UpdateEmail(val email: String) : Action
        data class UpdatePassword(val password: String) : Action
        data object DisplayLoginDialog : Action
        data object DismissLoginDialog : Action
        data object Login : Action
    }

    sealed interface Event {
        data class Error(val message: String) : Event
    }
}