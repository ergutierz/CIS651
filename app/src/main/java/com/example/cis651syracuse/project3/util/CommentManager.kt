package com.example.cis651syracuse.project3.util

import android.util.Log
import com.example.cis651syracuse.project3.model.Comment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentManager @Inject constructor(
    private val fireStore: FirebaseFirestore
) {

    private val commentsCollection = fireStore.collection("comments")

    fun addComment(comment: Comment, onComplete: (isSuccess: Boolean) -> Unit) {
        commentsCollection.add(comment)
            .addOnSuccessListener {
                onComplete(true)
                Log.d("CommentManager", "Comment added successfully!")
            }
            .addOnFailureListener { e ->
                onComplete(false)
                Log.w("CommentManager", "Error adding comment", e)
            }
    }

    fun deleteComment(commentId: String, onComplete: (isSuccess: Boolean) -> Unit) {
        commentsCollection.document(commentId).delete()
            .addOnSuccessListener {
                onComplete(true)
                Log.d("CommentManager", "Comment deleted successfully!")
            }
            .addOnFailureListener { e ->
                onComplete(false)
                Log.w("CommentManager", "Error deleting comment", e)
            }
    }

    fun getCommentsForPost(postId: String, onComplete: (isSuccess: Boolean, comments: List<Comment>) -> Unit) {
        commentsCollection
            .whereEqualTo("postId", postId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                val comments = result.documents.mapNotNull { it.toObject(Comment::class.java) }
                onComplete(true, comments)
            }
            .addOnFailureListener { e ->
                Log.w("CommentManager", "Error getting comments", e)
                onComplete(false, emptyList())
            }
    }
}
