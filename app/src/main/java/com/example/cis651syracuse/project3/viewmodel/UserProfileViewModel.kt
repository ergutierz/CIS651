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

    fun fetchUserProfile(userId: String) {
        setLoadingState(true)
        userManager.getUser(userId) { isSuccess, user ->
            viewModelScope.launch {
                _modelStore.process { oldState ->
                    setLoadingState(false)
                    if (isSuccess && user != null) {
                        oldState.copy(user = user)
                    } else {
                        oldState.copy(
                            event = ConsumableEvent.create(Event.Error("Unable to update user profile.")),
                        )
                    }
                }
            }
        }
    }

    fun updateUserProfile(userId: String, updates: Map<String, Any>) {
        setLoadingState(true)
        userManager.updateUser(userId, updates) { isSuccess ->
            setLoadingState(false)
            viewModelScope.launch {
                _modelStore.process { oldState ->
                    if (isSuccess) {
                        oldState.copy(updateSuccess = true)
                    } else {
                        oldState.copy(
                            event = ConsumableEvent.create(Event.Error("Unable to update user profile.")),
                        )
                    }
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
        val isLoading: Boolean = false,
        val updateSuccess: Boolean? = null,
        val event: ConsumableEvent<Event> = ConsumableEvent()
    )

    sealed interface Event {
        data class Error(val message: String) : Event
        // Define navigation events if needed
    }
}
