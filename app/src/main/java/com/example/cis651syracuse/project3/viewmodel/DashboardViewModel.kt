package com.example.cis651syracuse.project3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cis651syracuse.project3.util.PostManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.cis651syracuse.project3.model.Post

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val postManager: PostManager
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            postManager.getAllPosts { isSuccess, fetchedPosts ->
                if (isSuccess) {
                    _posts.value = fetchedPosts ?: emptyList()
                } else {
                    _posts.value = emptyList()
                }
            }
        }
    }
}

