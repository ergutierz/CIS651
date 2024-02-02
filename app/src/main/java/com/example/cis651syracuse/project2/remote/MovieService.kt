package com.example.cis651syracuse.project2.remote

import com.example.cis651syracuse.BuildConfig
import com.example.cis651syracuse.project2.model.PopularMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MovieService {
    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Header("Authorization") auth: String = "Bearer ${BuildConfig.TMDB_AUTH_TOKEN}"
    ): Response<PopularMoviesResponse>

}