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
        firebaseAuthenticationManager.signIn(email, password, onComplete)
    }

    fun register(email: String, password: String, onComplete: (FirebaseUser?, Exception?) -> Unit) {
        firebaseAuthenticationManager.signUp(email, password, onComplete)
    }
}