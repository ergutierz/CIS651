package com.example.cis651syracuse.project3.util

import android.util.Log
import com.example.cis651syracuse.project3.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostManager @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val firebaseAuthenticationManager: FirebaseAuthenticationManager
) {

    private val _selectedPostForEditing = MutableStateFlow<Post?>(null)
    val selectedPostForEditing = _selectedPostForEditing.asStateFlow()

    private val _postsFlow = MutableStateFlow<List<Post>>(emptyList())
    val postsFlow = _postsFlow.asStateFlow()

    private var registration: ListenerRegistration? = null

    fun listenToPostsUpdates() {
        val query: Query = fireStore.collection("posts")
        registration = query.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.w("PostManager", "Listen failed.", e)
                return@addSnapshotListener
            }

            val posts = snapshots?.documents?.mapNotNull { it.toObject(Post::class.java) } ?: emptyList()
            val loggedInUserId = firebaseAuthenticationManager.getCurrentUser?.uid.orEmpty()
            val postsWithLoggedInUser = posts.map { post -> post.copy(loggedInUserId = loggedInUserId) }
            _postsFlow.value = postsWithLoggedInUser
        }
    }

    fun clearPostsListener() {
        registration?.remove()
    }

    fun setSelectedPostForEditing(post: Post) {
        _selectedPostForEditing.update { post }
    }

    fun createPost(post: Post, onComplete: (isSuccess: Boolean) -> Unit) {
        fireStore.collection("posts").document(post.postId).set(post)
            .addOnSuccessListener {
                onComplete(true)
                Log.d("PostManager", "Post successfully created!")
            }
            .addOnFailureListener { e ->
                onComplete(false)
                Log.w("PostManager", "Error creating post", e)
            }
    }

    fun getAllPosts(onComplete: (isSuccess: Boolean, posts: List<Post>?) -> Unit) {
        fireStore.collection("posts").get()
            .addOnSuccessListener { querySnapshot ->
                val posts = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Post::class.java)
                }
                val loggedInUserId = firebaseAuthenticationManager.getCurrentUser?.uid.orEmpty()
                val postsWithLoggedInUser = posts.map { post -> post.copy(loggedInUserId = loggedInUserId) }
                onComplete(true, postsWithLoggedInUser)
            }
            .addOnFailureListener { exception ->
                Log.w("PostManager", "Error getting posts", exception)
                onComplete(false, null)
            }
    }


    fun getPost(postId: String, onComplete: (isSuccess: Boolean, post: Post?) -> Unit) {
        fireStore.collection("posts").document(postId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val post = document.toObject(Post::class.java)
                    onComplete(true, post)
                } else {
                    Log.d("PostManager", "No such post")
                    onComplete(false, null)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("PostManager", "get failed with ", exception)
                onComplete(false, null)
            }
    }

    fun updatePost(
        postId: String,
        updates: Map<String, Any>,
        onComplete: (isSuccess: Boolean) -> Unit
    ) {
        fireStore.collection("posts").document(postId).update(updates)
            .addOnSuccessListener {
                onComplete(true)
                Log.d("PostManager", "Post successfully updated!")
            }
            .addOnFailureListener { e ->
                onComplete(false)
                Log.w("PostManager", "Error updating post", e)
            }
    }

    fun deletePost(postId: String, onComplete: (isSuccess: Boolean) -> Unit) {
        fireStore.collection("posts").document(postId).delete()
            .addOnSuccessListener {
                onComplete(true)
                Log.d("PostManager", "Post successfully deleted!")
            }
            .addOnFailureListener { e ->
                onComplete(false)
                Log.w("PostManager", "Error deleting post", e)
            }
    }

    fun uploadImage(
        postId: String,
        imageData: ByteArray,
        onComplete: (isSuccess: Boolean, imageUrl: String?) -> Unit
    ) {
        val storageRef = storage.getReference("post_images/$postId")
        val uploadTask = storageRef.putBytes(imageData)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onComplete(true, uri.toString())
            }.addOnFailureListener { e ->
                onComplete(false, null)
                Log.e("PostManager", "Error getting download URL", e)
            }
        }.addOnFailureListener { e ->
            onComplete(false, null)
            Log.e("PostManager", "Error uploading image", e)
        }
    }
}
