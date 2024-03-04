package com.example.cis651syracuse.project3.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cis651syracuse.core.ConsumableEvent
import com.example.cis651syracuse.core.ModelStore
import com.example.cis651syracuse.core.StateFlowModelStore
import com.example.cis651syracuse.project3.model.Post
import com.example.cis651syracuse.project3.util.NavigationCommandManager
import com.example.cis651syracuse.project3.util.PostManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel @Inject constructor(
    private val postManager: PostManager,
    private val navigationCommandManager: NavigationCommandManager
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
        viewModelScope.launch {
            postManager.selectedPostForEditing.onEach { selectedPostForEditing ->
                _modelStore.process { oldState ->
                    oldState.copy(
                        selectedPostForEditing = selectedPostForEditing,
                        description = selectedPostForEditing?.description,
                        imageUri = Uri.parse(selectedPostForEditing?.imageUrl)
                    )
                }
            }.launchIn(this)
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.UpdatePost -> updatePost()
            is Action.DeletePost -> deletePost()
            is Action.UpdateDescription -> updateDescription(action.description)
            is Action.UpdateImageUri -> updateImageUri(action.imageUri)
            is Action.LoadPost -> loadPost()
        }
    }

    private fun loadPost() {
        viewModelScope.launch {
            val selectedPostForEditing = postManager.selectedPostForEditing.value
            _modelStore.process { oldState ->
                oldState.copy(
                    selectedPostForEditing = selectedPostForEditing,
                    description = selectedPostForEditing?.description,
                    imageUri = Uri.parse(selectedPostForEditing?.imageUrl)
                )
            }
        }
    }

    private fun updateImageUri(imageUri: Uri) {
        viewModelScope.launch {
            _modelStore.process { oldState ->
                oldState.copy(imageUri = imageUri)
            }
        }
    }

    private fun updateDescription(description: String) {
        viewModelScope.launch {
            _modelStore.process { oldState ->
                oldState.copy(description = description)
            }
        }
    }

    private fun updatePost() {
        val description = _modelStore.value.description.orEmpty()
        val imageUrl = _modelStore.value.imageUri.toString()
        val updates = mapOf("description" to description, "imageUrl" to imageUrl)
        val postId = _modelStore.value.selectedPostForEditing?.postId.orEmpty()
        setLoadingState(true)
        postManager.updatePost(postId, updates) { isSuccess ->
            viewModelScope.launch {
                setLoadingState(false)
                if (isSuccess) {
                    _modelStore.process { oldState ->
                        oldState.copy(
                            consumableEvent = ConsumableEvent.create(Event.PostEditSuccess)
                        )
                    }
                } else {
                    _modelStore.process { oldState ->
                        oldState.copy(
                            consumableEvent = ConsumableEvent.create(Event.PostEditFailure("Failed to update post"))
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

    private fun deletePost() {
        val postId = _modelStore.value.selectedPostForEditing?.postId.orEmpty()
        setLoadingState(true)
        postManager.deletePost(postId) { isSuccess ->
            setLoadingState(false)
            viewModelScope.launch {
                if (isSuccess) {
                    navigationCommandManager.navigate(NavigationCommandManager.dashboardDirection)
                } else {
                    _modelStore.process { oldState ->
                        oldState.copy(
                            consumableEvent = ConsumableEvent.create(Event.PostEditFailure("Failed to delete post"))
                        )
                    }
                }
            }
        }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val selectedPostForEditing: Post? = null,
        val imageUri: Uri? = null,
        val description: String? = null,
        val consumableEvent: ConsumableEvent<Event> = ConsumableEvent()
    )

    sealed interface Action {
        data class UpdateImageUri(val imageUri: Uri) : Action
        data class UpdateDescription(val description: String) : Action
        data object UpdatePost : Action
        data object DeletePost : Action
        data object LoadPost : Action
    }

    sealed interface Event {
        data object PostEditSuccess : Event
        data class PostEditFailure(val message: String) : Event
    }
}
