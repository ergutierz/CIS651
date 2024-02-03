package com.example.cis651syracuse.project2.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun MovieCard(
    modifier: Modifier = Modifier,
    movie: Movie,
    onAction: (action: MovieListViewModel.Action) -> Unit = {}
) {
    val typography = Typography(
        h6 = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFFFFD700)),
        body2 = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.White)
    )

    Card(
        modifier = modifier.padding(8.dp).clickable {
            onAction(MovieListViewModel.Action.DisplayMovieDetail(movie.id ?: -1))
        },
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
                    modifier = Modifier.fillMaxWidth().height(300.dp).padding(all = 16.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Text(text = movie.title ?: "", style = typography.h6, modifier = Modifier.padding(8.dp))
            Text(text = "Release date: ${movie.releaseDate}", style = typography.body2, modifier = Modifier.padding(horizontal = 8.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = movie.overview ?: "",
                style = typography.body2,
                modifier = Modifier.padding(8.dp).heightIn(0.dp, 100.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 4
            )
        }
    }
}
