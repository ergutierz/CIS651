package com.example.cis651syracuse.project3.util

import android.util.Log
import com.example.cis651syracuse.project3.model.User
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val fireStore: FirebaseFirestore
) {

    fun createUser(user: User, onComplete: (isSuccess: Boolean) -> Unit) {
        fireStore.collection("users").document(user.userId).set(user)
            .addOnSuccessListener {
                onComplete(true)
                Log.d("UserManager", "User successfully written!")
            }
            .addOnFailureListener { e ->
                onComplete(false)
                Log.w("UserManager", "Error writing user", e)
            }
    }

    fun getUser(userId: String, onComplete: (isSuccess: Boolean, user: User?) -> Unit) {
        fireStore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(User::class.java)
                    onComplete(true, user)
                } else {
                    Log.d("UserManager", "No such document")
                    onComplete(false, null)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("UserManager", "get failed with ", exception)
                onComplete(false, null)
            }
    }

    fun updateUser(userId: String, updates: Map<String, Any>, onComplete: (isSuccess: Boolean) -> Unit) {
        fireStore.collection("users").document(userId).update(updates)
            .addOnSuccessListener {
                onComplete(true)
                Log.d("UserManager", "User successfully updated!")
            }
            .addOnFailureListener { e ->
                onComplete(false)
                Log.w("UserManager", "Error updating user", e)
            }
    }

    fun deleteUser(userId: String, onComplete: (isSuccess: Boolean) -> Unit) {
        fireStore.collection("users").document(userId).delete()
            .addOnSuccessListener {
                onComplete(true)
                Log.d("UserManager", "User successfully deleted!")
            }
            .addOnFailureListener { e ->
                onComplete(false)
                Log.w("UserManager", "Error deleting user", e)
            }
    }
}
