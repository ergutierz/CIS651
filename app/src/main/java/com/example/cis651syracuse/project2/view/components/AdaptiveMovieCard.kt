package com.example.cis651syracuse.project2.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cis651syracuse.BuildConfig
import com.example.cis651syracuse.project2.model.Movie
import com.example.cis651syracuse.project2.viewmodel.MovieListViewModel
import kotlinx.coroutines.Dispatchers

@Composable
fun AdaptiveMovieCard(
    movie: Movie,
    onAction: (MovieListViewModel.Action) -> Unit = {},
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val typography = adaptiveTypography(maxWidth)

        MovieCard(movie, onAction, typography)
    }
}

private fun adaptiveTypography(maxWidth: androidx.compose.ui.unit.Dp): Typography {
    val textStyle = if (maxWidth < 600.dp) {
        TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFFFD700))
    } else {
        TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFFFFD700))
    }

    return Typography(h6 = textStyle, body2 = textStyle.copy(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 14.sp))
}

@Composable
private fun MovieCard(
    movie: Movie,
    onAction: (MovieListViewModel.Action) -> Unit,
    typography: Typography
) {
    Card(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
            .clickable { onAction(MovieListViewModel.Action.DisplayMovieDetail(movie.id ?: -1)) },
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.Black,
        border = BorderStroke(1.dp, Color(0xFFFFD700))
    ) {
        Column {
            movie.posterPath?.let { path ->
                val imageUrl = "${BuildConfig.TMBD_POSTER_URL_BASE}$path"
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .dispatcher(Dispatchers.IO)
                        .memoryCacheKey(imageUrl)
                        .diskCacheKey(imageUrl)
                        .data(imageUrl)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = "${movie.title} poster",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 180.dp, max = 300.dp) // Adaptive height based on content
                        .padding(all = 16.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Text(text = movie.title ?: "", style = typography.h6, modifier = Modifier.padding(8.dp))
            Text(text = "Release date: ${movie.releaseDate}", style = typography.body2, modifier = Modifier.padding(horizontal = 8.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = movie.overview ?: "",
                style = typography.body2,
                modifier = Modifier
                    .padding(8.dp)
                    .heightIn(min = 60.dp, max = 100.dp), // Adaptive text box height
                overflow = TextOverflow.Ellipsis,
                maxLines = 4
            )
        }
    }
}
