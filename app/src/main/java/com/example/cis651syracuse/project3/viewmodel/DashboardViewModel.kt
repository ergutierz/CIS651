package com.example.cis651syracuse.project3.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cis651syracuse.project3.model.Post
import com.example.cis651syracuse.project3.util.NavigationCommandManager
import com.example.cis651syracuse.project3.util.PostManager
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val postManager: PostManager,
    private val navigationCommandManager: NavigationCommandManager
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = postManager.postsFlow
//    private var registration: ListenerRegistration? = null

//    init {
//        fetchPosts()
//    }
//
//    private fun fetchPosts() {
//        viewModelScope.launch {
//            postManager.getAllPosts { isSuccess, fetchedPosts ->
//                if (isSuccess) {
//                    _posts.value = fetchedPosts ?: emptyList()
//                } else {
//                    _posts.value = emptyList()
//                }
//                subscribeToUpdates()
//            }
//        }
//    }

//    private fun subscribeToUpdates() {
//        val db = FirebaseFirestore.getInstance()
//        val query: Query = db.collection("posts")
//        registration = query.addSnapshotListener { value, error ->
//            if (error != null) {
//                Log.w("PostManager", "Listen failed.", error)
//                return@addSnapshotListener
//            }
//
//            val posts = value?.documents?.mapNotNull { it.toObject(Post::class.java) } ?: emptyList()
//            _posts.value = posts
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        postManager.clearPostsListener()
    }

    fun subscribeToUpdates() {
        postManager.listenToPostsUpdates()
    }

    fun deletePost(post: Post) {
        postManager.deletePost(post.postId) { isSuccess ->
//            if (isSuccess) {
//                fetchPosts()
//            }
        }
    }

    fun editPost(post: Post) {
        postManager.setSelectedPostForEditing(post)
        navigationCommandManager.navigate(NavigationCommandManager.editPostDirection)
    }
}

