package com.example.cis651syracuse.project2.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PopularMoviesResponse(
    @Json(name = "page")
    val page: Int = 0,

    @Json(name = "results")
    val results: List<Movie> = emptyList(),

    @Json(name = "total_pages")
    val total_pages: Int = 0,

    @Json(name = "total_results")
    val total_results: Int = 0
)

@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "adult")
    val adult: Boolean = false,

    @Json(name = "backdrop_path")
    val backdrop_path: String? = null,

    @Json(name = "genre_ids")
    val genre_ids: List<Int> = emptyList(),

    @Json(name = "id")
    val id: Int = 0,

    @Json(name = "original_language")
    val original_language: String? = null,

    @Json(name = "original_title")
    val original_title: String? = null,

    @Json(name = "overview")
    val overview: String? = null,

    @Json(name = "popularity")
    val popularity: Double = 0.0,

    @Json(name = "poster_path")
    val poster_path: String? = null,

    @Json(name = "release_date")
    val release_date: String? = null,

    @Json(name = "title")
    val title: String? = null,

    @Json(name = "video")
    val video: Boolean = false,

    @Json(name = "vote_average")
    val vote_average: Double = 0.0,

    @Json(name = "vote_count")
    val vote_count: Int = 0
)

@JsonClass(generateAdapter = true)
data class Icon(
    @Json(name = "url")
    val url: String? = null
)
