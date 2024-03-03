package com.example.cis651syracuse.project3.model

data class User(
    val userId: String = "",
    val email: String = "",
    val displayName: String = "",
    val phoneNumber: String? = null,
    val profilePictureUrl: String? = null
)
