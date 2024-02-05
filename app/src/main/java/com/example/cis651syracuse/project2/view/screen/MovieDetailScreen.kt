package com.example.cis651syracuse.project2.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cis651syracuse.BuildConfig
import com.example.cis651syracuse.R
import com.example.cis651syracuse.core.DeviceUtils
import com.example.cis651syracuse.project2.model.MovieDetailResponse
import com.example.cis651syracuse.project2.view.components.ErrorScreen
import kotlinx.coroutines.Dispatchers

@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    movieDetail: MovieDetailResponse?
) {
    val isLandscape = DeviceUtils.isLandscape(LocalContext.current)
    val (imageModifier, contentScale) = if (isLandscape) {
        Pair(
            Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f), ContentScale.Fit)
    } else {
        Pair(Modifier.fillMaxWidth(), ContentScale.Crop)
    }
    val typography = Typography(
        h4 = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 28.sp, color = Color(0xFFFFD700)),
        body1 = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.White),
        subtitle1 = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Light, fontSize = 14.sp, color = Color(0xFFCCCCCC))
    )

    if (movieDetail != null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.Black)
                .padding(16.dp)
        ) {
            movieDetail.posterPath?.let { path ->
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
                    contentDescription = stringResource(R.string.movie_poster, movieDetail.title.orEmpty()),
                    modifier = imageModifier,
                    contentScale = contentScale
                )
            }

            Text(
                text = movieDetail.title.orEmpty(),
                style = typography.h4
            )

            Text(
                text = stringResource(id =R.string.release_date_moviedetail_relea, movieDetail.releaseDate.orEmpty()),
                style = typography.subtitle1
            )

            Spacer(Modifier.height(8.dp))

            movieDetail.genres?.let { genres ->
                Text(
                    text = stringResource(id = R.string.genres_genres_jointostring_it_, genres.joinToString { it?.name.orEmpty() }),
                    style = typography.subtitle1
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.runtime_moviedetail_runtime_mi, movieDetail.runtime.toString()),
                style = typography.subtitle1
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.overview_moviedetail_overview, movieDetail.overview.orEmpty()),
                style = typography.body1
            )
        }
    } else ErrorScreen()
}