package com.example.cis651syracuse.project3.repository

import com.example.cis651syracuse.project3.util.FirebaseAuthenticationManager
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepository @Inject constructor(
    private val firebaseAuthenticationManager: FirebaseAuthenticationManager
) {

    fun login(email: String, password: String, onComplete: (FirebaseUser?, Exception?) -> Unit) {
        try {
            firebaseAuthenticationManager.signIn(email, password, onComplete)
        } catch (e: Exception) {
            onComplete(null, e)
        }
    }

    fun register(email: String, password: String, onComplete: (FirebaseUser?, Exception?) -> Unit) {
        try {
            firebaseAuthenticationManager.signUp(email, password, onComplete)
        } catch (e: Exception) {
            onComplete(null, e)
        }
    }

    fun sendPasswordReset(email: String, onComplete: (Exception?) -> Unit) {
        try {
            firebaseAuthenticationManager.sendPasswordReset(email, onComplete)
        } catch (e: Exception) {
            onComplete(e)
        }
    }

    fun signOut() {
        firebaseAuthenticationManager.signOut()
    }

    val getCurrentUser: FirebaseUser? get() = firebaseAuthenticationManager.getCurrentUser
}