package com.example.cis651syracuse.project2.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthenticationResponse(
	@Json(name = "status_message")
	val statusMessage: String? = null,

	@Json(name = "status_code")
	val statusCode: Int? = null,

	@Json(name = "success")
	val success: Boolean? = null
)

