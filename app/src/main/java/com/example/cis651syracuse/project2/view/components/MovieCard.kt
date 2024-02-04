package com.example.cis651syracuse.project2.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.foundation.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.cis651syracuse.BuildConfig
import com.example.cis651syracuse.project2.model.Movie
import kotlinx.coroutines.Dispatchers

@Composable
fun MovieCard(
    movie: Movie
) {
    val typography = Typography(
        h6 = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFFFFD700)),
        body2 = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.White)
    )

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.Black,
        border = BorderStroke(1.dp, Color(0xFFFFD700))
    ) {
        if (isLandscape) {
            Row {
                MovieImage(path = movie.posterPath)
                MovieTextContent(movie = movie, typography = typography)
            }
        } else {
            Column {
                MovieImage(path = movie.posterPath)
                MovieTextContent(movie = movie, typography = typography)
            }
        }
    }
}

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
