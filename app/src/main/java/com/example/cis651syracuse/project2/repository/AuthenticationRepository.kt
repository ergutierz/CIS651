package com.example.cis651syracuse.project2.repository

import android.util.Log
import com.example.cis651syracuse.project2.model.AuthenticationResponse
import com.example.cis651syracuse.project2.remote.AuthenticationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepository @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    suspend fun authenticate() = withContext(Dispatchers.IO) {
        runCatching { authenticationService.authenticate() }.fold(
            onSuccess = { response ->
                val responseBody = response.body()
                if (responseBody?.success == true) {
                    Log.e("AuthenticationRepository", "Authentication successful")
                } else {
                    Log.e("AuthenticationRepository", "Authentication failed with message: ${responseBody?.statusMessage}")
                }
            },
            onFailure = {
                Log.e("AuthenticationRepository", "Failed to authenticate", it)
            }
        )
    }
}
