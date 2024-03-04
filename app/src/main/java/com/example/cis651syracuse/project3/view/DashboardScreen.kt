package com.example.cis651syracuse.project3.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.cis651syracuse.project3.model.Post
import com.example.cis651syracuse.project3.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = hiltViewModel()
    val posts by viewModel.posts.collectAsState()

    LazyColumn {
        items(posts) { post ->
            PostItem(post = post)
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row {
                post.imageUrl?.let { imageUrl ->
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Post Image",
                        modifier = Modifier
                            .size(150.dp)
                            .padding(4.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = post.description,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Likes: ${post.likeCount}",
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = "Posted on: ${formatDate(post.timestamp)}",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}

private fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return formatter.format(date)
}

