package com.example.cis651syracuse.project2.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import com.example.cis651syracuse.BuildConfig
import com.example.cis651syracuse.project2.model.Movie
import kotlinx.coroutines.Dispatchers

@Composable
fun MovieImage(path: String?) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val widthModifier = if (isLandscape) Modifier.width(200.dp) else Modifier.fillMaxWidth()
    val heightModifier = Modifier.height(300.dp)
    path?.let { imagePath ->
        val imageUrl = "${BuildConfig.TMBD_POSTER_URL_BASE}$imagePath"
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .dispatcher(Dispatchers.IO)
                .data(imageUrl)
                .build(),
            contentDescription = "Movie Poster",
            modifier = Modifier
                .then(heightModifier)
                .then(widthModifier)
                .padding(16.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun MovieTextContent(movie: Movie, typography: Typography) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = movie.title ?: "", style = typography.h6)
        Text(text = "Release date: ${movie.releaseDate}", style = typography.body2)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = movie.overview ?: "",
            style = typography.body2,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
    }
}
