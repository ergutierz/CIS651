package com.example.cis651syracuse.project3.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.cis651syracuse.project3.model.Post
import com.example.cis651syracuse.project3.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = hiltViewModel()
    val posts by viewModel.posts.collectAsState()
    val displayTryAgain by viewModel.displayTryAgain.collectAsState()

    LazyColumn {
        items(posts) { post ->
            PostItem(
                post = post,
                onEditClick = viewModel::editPost,
                onDeleteClick = viewModel::deletePost,
                onLikeClick = viewModel::likePost
            )
        }
    }
    if (displayTryAgain.value is DashboardViewModel.Event.TryAgainLater) TryAgainLaterToast()
    LaunchedEffect("posts") {
        viewModel.subscribeToUpdates()
    }
}

@Composable
private fun TryAgainLaterToast() {
    Toast.makeText(
        LocalContext.current,
        "Failed to delete post. Please try again later.",
        Toast.LENGTH_SHORT
    ).show()
}

@Composable
private fun PostItem(
    post: Post,
    onEditClick: (Post) -> Unit,
    onDeleteClick: (Post) -> Unit,
    onLikeClick: (Boolean, Post) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .size(150.dp)
            .padding(8.dp)
            .clickable(enabled = post.postBelongsToLoggedInUser) {},
        elevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            UserInfoSection(post)
            CardContent(post, onEditClick, onDeleteClick, onLikeClick)
        }
    }
}

@Composable
private fun UserInfoSection(post: Post) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (!post.imageUrl.isNullOrBlank() && post.imageUrl != "null") {
            AsyncImage(
                model = post.imageUrl,
                contentDescription = "Post Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Face,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
            )
        }
        Text(text = "@${post.handle ?: "Unknown"}")
    }
}

@Composable
private fun CardContent(
    post: Post,
    onEditClick: (Post) -> Unit,
    onDeleteClick: (Post) -> Unit,
    onLikeClick: (Boolean, Post) -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = post.description, style = MaterialTheme.typography.body1)
        Spacer(Modifier.weight(1f))
        Text(text = "Likes: ${post.likeCount}", style = MaterialTheme.typography.body2)
        Text(
            text = "Posted on: ${formatDate(post.timestamp)}",
            style = MaterialTheme.typography.body2
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row {
                if (post.postBelongsToLoggedInUser) {
                    Button(onClick = { onEditClick(post) }, shape = RoundedCornerShape(50)) {
                        Text(text = "Edit")
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(onClick = { onDeleteClick(post) }, shape = RoundedCornerShape(50)) {
                        Text(text = "Delete")
                    }
                }
            }
            IconButton(onClick = {
                isLiked = !isLiked
                onLikeClick(isLiked, post)
            }) {
                Icon(
                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (isLiked) Color.Red else Color.Gray
                )
            }
        }
    }
}

private fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return formatter.format(date)
}

