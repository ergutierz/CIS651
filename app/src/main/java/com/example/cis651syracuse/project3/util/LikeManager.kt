package com.example.cis651syracuse.project3.util

import android.util.Log
import com.example.cis651syracuse.project3.model.Like
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LikeManager @Inject constructor(
    private val fireStore: FirebaseFirestore
) {

    private val likesCollection = fireStore.collection("likes")

    fun addLike(postId: String, userId: String, onComplete: (isSuccess: Boolean) -> Unit) {
        val like = Like(likeId = "${userId}_${postId}_${System.currentTimeMillis()}", postId = postId, userId = userId)

        likesCollection.add(like)
            .addOnSuccessListener {
                onComplete(true)
                Log.d("LikeManager", "Like added successfully!")
            }
            .addOnFailureListener { e ->
                onComplete(false)
                Log.w("LikeManager", "Error adding like", e)
            }
    }

    fun removeLike(likeId: String, onComplete: (isSuccess: Boolean) -> Unit) {
        likesCollection.document(likeId).delete()
            .addOnSuccessListener {
                onComplete(true)
                Log.d("LikeManager", "Like removed successfully!")
            }
            .addOnFailureListener { e ->
                onComplete(false)
                Log.w("LikeManager", "Error removing like", e)
            }
    }

    fun checkIfLiked(postId: String, userId: String, onComplete: (isLiked: Boolean) -> Unit) {
        likesCollection
            .whereEqualTo("postId", postId)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                onComplete(!result.isEmpty)  // True if a like exists
            }
            .addOnFailureListener { e ->
                Log.w("LikeManager", "Error checking like", e)
                onComplete(false)
            }
    }

    fun getLikesForPost(postId: String, onComplete: (isSuccess: Boolean, likes: List<Like>) -> Unit) {
        likesCollection
            .whereEqualTo("postId", postId)
            .get()
            .addOnSuccessListener { result ->
                val likes = result.documents.mapNotNull { it.toObject(Like::class.java) }
                onComplete(true, likes)
            }
            .addOnFailureListener { e ->
                Log.w("LikeManager", "Error getting likes", e)
                onComplete(false, emptyList())
            }
    }
}
