package com.example.cis651syracuse.project3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cis651syracuse.core.ConsumableEvent
import com.example.cis651syracuse.core.ModelStore
import com.example.cis651syracuse.core.StateFlowModelStore
import com.example.cis651syracuse.project3.util.UserManager
import com.example.cis651syracuse.project3.model.User
import com.example.cis651syracuse.project3.util.FirebaseAuthenticationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userManager: UserManager,
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
            is Action.UpdateUserProfile -> updateUserProfile()
            is Action.UpdateEmail -> updateEmail(action.email)
            is Action.UpdateDisplayName -> updateDisplayName(action.displayName)
            is Action.UpdatePhoneNumber -> updatePhoneNumber(action.phoneNumber)
        }
    }

    private fun updateEmail(email: String) {
        viewModelScope.launch {
            _modelStore.process { oldState -> oldState.copy(email = email) }
        }
    }

    private fun updateDisplayName(displayName: String) {
        viewModelScope.launch {
            _modelStore.process { oldState -> oldState.copy(displayName = displayName) }
        }
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            _modelStore.process { oldState -> oldState.copy(phoneNumber = phoneNumber) }
        }
    }

    private fun fetchUserProfile() {
        val userId = firebaseAuthenticationManager.getCurrentUser?.uid.orEmpty()
        setLoadingState(true)
        userManager.getUser(userId) { isSuccess, user ->
            viewModelScope.launch {
                _modelStore.process { oldState ->
                    setLoadingState(false)
                    if (isSuccess && user != null) {
                        oldState.copy(user = user)
                    } else {
                        oldState.copy(
                            consumableEvent = ConsumableEvent.create(Event.Error("Unable to update user profile.")),
                        )
                    }
                }
            }
        }
    }

    private fun updateUserProfile() {
        val userId = firebaseAuthenticationManager.getCurrentUser?.uid.orEmpty()
        val updates = mapOf(
            "email" to _modelStore.value.email.orEmpty(),
            "displayName" to _modelStore.value.displayName.orEmpty(),
            "phoneNumber" to _modelStore.value.phoneNumber.orEmpty()
        )
        setLoadingState(true)
        userManager.updateUser(userId, updates) { isSuccess ->
            setLoadingState(false)
            viewModelScope.launch {
                _modelStore.process { oldState ->
                    oldState.copy(
                        consumableEvent = ConsumableEvent.create(
                            Event.Success("Profile updated successfully").takeIf { isSuccess } ?:
                            Event.Error("Unable to update user profile.")
                        )
                    )
                }
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        viewModelScope.launch {
            _modelStore.process { oldState -> oldState.copy(isLoading = isLoading) }
        }
    }

    data class ViewState(
        val user: User? = null,
        val email: String? = null,
        val displayName: String? = null,
        val phoneNumber: String? = null,
        val isLoading: Boolean = false,
        val consumableEvent: ConsumableEvent<Event> = ConsumableEvent()
    )

    sealed interface Action {
        data object UpdateUserProfile : Action
        data class UpdateEmail(val email: String) : Action
        data class UpdateDisplayName(val displayName: String) : Action
        data class UpdatePhoneNumber(val phoneNumber: String) : Action
    }

    sealed interface Event {
        data class Error(val message: String) : Event
        data class Success(val message: String) : Event
    }
}
