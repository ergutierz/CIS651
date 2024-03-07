package com.example.cis651syracuse.project3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cis651syracuse.core.ConsumableEvent
import com.example.cis651syracuse.core.ModelStore
import com.example.cis651syracuse.core.StateFlowModelStore
import com.example.cis651syracuse.project3.util.PostManager
import com.example.cis651syracuse.project3.model.Post
import com.example.cis651syracuse.project3.util.FirebaseAuthenticationManager
import com.google.type.Date
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PostCreationViewModel @Inject constructor(
    private val postManager: PostManager,
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

    fun onAction(action: Action) {
        when (action) {
            is Action.CreatePost -> createPost(action.description, action.imageUri)
        }
    }

    private fun createPost(description: String?, imageUrl: String?) {
        setLoadingState(true)
        val userId = firebaseAuthenticationManager.getCurrentUser?.uid.orEmpty()
        val emailHandle = firebaseAuthenticationManager.getCurrentUser?.email?.substringBefore("@").orEmpty()
        val post = Post(
            handle = emailHandle,
            userId = userId,
            postId = UUID.randomUUID().toString(),
            description = description.orEmpty(),
            imageUrl = imageUrl.orEmpty(),
        )
        postManager.createPost(post) { isSuccess ->
            setLoadingState(false)
            viewModelScope.launch {
                _modelStore.process { oldState ->
                    if (isSuccess) {
                        oldState.copy(
                            isPostCreated = true,
                            consumableEvent = ConsumableEvent.create(Event.PostCreationSuccess)
                        )
                    } else {
                        oldState.copy(
                            consumableEvent = ConsumableEvent.create(Event.Error("Failed to create post"))
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
        val isLoading: Boolean = false,
        val isPostCreated: Boolean = false,
        val consumableEvent: ConsumableEvent<Event> = ConsumableEvent(),
    )

    sealed interface Action {
        data class CreatePost(
            val imageUri: String? = null,
            val description: String? = null
        ) : Action
    }

    sealed interface Event {
        data object PostCreationSuccess : Event
        data class Error(val message: String) : Event
    }
}
