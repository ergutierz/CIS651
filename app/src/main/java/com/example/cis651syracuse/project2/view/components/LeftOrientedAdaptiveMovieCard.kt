package com.example.cis651syracuse.project2.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cis651syracuse.R
import com.example.cis651syracuse.BuildConfig
import com.example.cis651syracuse.project2.model.Movie
import com.example.cis651syracuse.project2.viewmodel.MovieListViewModel
import kotlinx.coroutines.Dispatchers

@Composable
fun LeftOrientedAdaptiveMovieCard(
    movie: Movie,
    onAction: (MovieListViewModel.Action) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .height(200.dp)
            .clickable { onAction(MovieListViewModel.Action.DisplayMovieDetail(movie.id ?: -1)) },
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.Black
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
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
                    contentDescription = stringResource(id = R.string.movie_title_poster, movie.title.orEmpty()),
                    modifier = Modifier
                        .width(130.dp)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = movie.title.orEmpty(),
                    style = MaterialTheme.typography.h6.copy(color = Color(0xFFFFD700)),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.release_date_moviedetail_relea, movie.releaseDate.orEmpty()),
                    style = MaterialTheme.typography.body2.copy(color = Color.White),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.overview.orEmpty(),
                    style = MaterialTheme.typography.body2.copy(color = Color.White),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
