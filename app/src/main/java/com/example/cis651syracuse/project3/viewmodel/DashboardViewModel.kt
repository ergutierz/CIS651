package com.example.cis651syracuse.project3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cis651syracuse.core.ConsumableEvent
import com.example.cis651syracuse.project3.model.Post
import com.example.cis651syracuse.project3.util.NavigationCommandManager
import com.example.cis651syracuse.project3.util.PostManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val postManager: PostManager,
    private val navigationCommandManager: NavigationCommandManager
) : ViewModel() {

    private val _displayTryAgain = MutableStateFlow<ConsumableEvent<Event>>(ConsumableEvent())
    val displayTryAgain: StateFlow<ConsumableEvent<Event>> = _displayTryAgain.asStateFlow()
    val posts: StateFlow<List<Post>> = postManager.postsFlow

    override fun onCleared() {
        super.onCleared()
        postManager.clearPostsListener()
    }

    fun subscribeToUpdates() {
        postManager.listenToPostsUpdates()
    }

    fun deletePost(post: Post) {
        postManager.deletePost(post.postId) { isSuccess ->
            if (!isSuccess) {
                _displayTryAgain.value = ConsumableEvent.create(Event.TryAgainLater)
            }
        }
    }

    fun editPost(post: Post) {
        postManager.setSelectedPostForEditing(post)
        navigationCommandManager.navigate(NavigationCommandManager.editPostDirection)
    }

    fun likePost(isLiked: Boolean, post: Post) {
        postManager.likePost(isLiked, post)
    }

    sealed interface Event {
        data object TryAgainLater : Event
    }
}

