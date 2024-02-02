package com.example.cis651syracuse.project2.remote

import com.example.cis651syracuse.BuildConfig
import com.example.cis651syracuse.project2.model.AuthenticationResponse
import com.example.cis651syracuse.project2.model.PopularMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AuthenticationService {
    @GET("3/authentication/token/new")
    suspend fun authenticate(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<AuthenticationResponse>
}
