package com.example.cis651syracuse.project2.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cis651syracuse.BuildConfig
import com.example.cis651syracuse.project2.model.MovieDetailResponse
import com.example.cis651syracuse.core.adaptiveTypography

@Composable
fun AdaptiveMovieDetailScreen(
    movieDetail: MovieDetailResponse?,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val typography = adaptiveTypography(maxWidth)

        if (movieDetail != null) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(16.dp)
            ) {
                movieDetail.posterPath?.let { path ->
                    AsyncImage(
                        model = "${BuildConfig.TMBD_POSTER_URL_BASE}$path",
                        contentDescription = "${movieDetail.title} poster",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(bottom = 16.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = movieDetail.title ?: "",
                    style = typography.h4
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Release date: ${movieDetail.releaseDate}",
                    style = typography.subtitle1
                )

                Spacer(Modifier.height(8.dp))

                movieDetail.genres?.let { genres ->
                    Text(
                        text = "Genres: ${genres.joinToString { it?.name ?: "" }}",
                        style = typography.subtitle1
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Runtime: ${movieDetail.runtime ?: 0} minutes",
                    style = typography.subtitle1
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = movieDetail.overview ?: "",
                    style = typography.body1
                )
            }
        } else {
            ErrorScreen()
        }
    }
}
