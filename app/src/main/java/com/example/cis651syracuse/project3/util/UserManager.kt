package com.example.cis651syracuse.project3.util

import android.util.Log
import com.example.cis651syracuse.project3.model.User
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseAuthenticationManager: FirebaseAuthenticationManager
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

    fun updateUserProfile(userId: String, updates: Map<String, Any>, onComplete: (isSuccess: Boolean) -> Unit) {
        val docRef = fireStore.collection("users").document(userId)
        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                docRef.update(updates)
                    .addOnSuccessListener { onComplete(true) }
                    .addOnFailureListener { e ->
                        Log.w("UserManager", "Error updating user", e)
                        onComplete(false)
                    }
            } else {
                val newUserUpdates = updates.toMutableMap().apply {
                    this["userId"] = userId
                    this["email"] = firebaseAuthenticationManager.getCurrentUser?.email.orEmpty()
                }
                docRef.set(newUserUpdates)
                    .addOnSuccessListener {
                        Log.d("UserManager", "User document created successfully")
                        onComplete(true)
                    }
                    .addOnFailureListener { e ->
                        Log.w("UserManager", "Error creating user document", e)
                        onComplete(false)
                    }
            }
        }.addOnFailureListener { e ->
            Log.w("UserManager", "Error checking user document", e)
            onComplete(false)
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
