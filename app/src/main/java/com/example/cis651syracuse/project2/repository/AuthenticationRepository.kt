package com.example.cis651syracuse.project2.repository

import com.example.cis651syracuse.project2.model.AuthenticationResponse
import com.example.cis651syracuse.project2.remote.AuthenticationService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepository @Inject constructor(
    private val service: AuthenticationService
) {
    suspend fun authenticate(): AuthenticationResponse {
        val response = service.authenticate()
        if (response.isSuccessful) {
            response.body()?.let {
                return it
            }
        }
        throw Exception("Authentication failed")
    }
}
