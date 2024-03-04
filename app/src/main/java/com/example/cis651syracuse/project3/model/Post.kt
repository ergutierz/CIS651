package com.example.cis651syracuse.project3.model

import java.util.Date

data class Post(
    val postId: String = "",
    val userId: String = "",
    val loggedInUserId: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    val timestamp: Date = Date(),
    val likeCount: Int = 0
) {
    val postBelongsToLoggedInUser: Boolean
        get() = userId == loggedInUserId
}
