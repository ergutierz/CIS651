package com.example.cis651syracuse.project3.model

import com.google.type.Date

data class Comment(
    val commentId: String,
    val postId: String,
    val userId: String,
    val commentText: String,
    val timestamp: Date
)
