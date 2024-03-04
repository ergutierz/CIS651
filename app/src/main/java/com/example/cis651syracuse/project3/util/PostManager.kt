package com.example.cis651syracuse.project3.util

import android.util.Log
import com.example.cis651syracuse.project3.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostManager @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

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
                onComplete(true, posts)
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

    fun updatePost(postId: String, updates: Map<String, Any>, onComplete: (isSuccess: Boolean) -> Unit) {
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

    fun uploadImage(postId: String, imageData: ByteArray, onComplete: (isSuccess: Boolean, imageUrl: String?) -> Unit) {
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
