package com.example.cis651syracuse.project3.model

import com.google.type.Date

data class Post(
    val postId: String,
    val userId: String,
    val description: String,
    val imageUrl: String? = null,
    val timestamp: Date,
    val likeCount: Int = 0
)
