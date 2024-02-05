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
import androidx.compose.ui.res.stringResource
import com.example.cis651syracuse.BuildConfig
import com.example.cis651syracuse.R
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
            contentDescription = stringResource(id = R.string.movie_poster),
            modifier = Modifier
                .then(heightModifier)
                .then(widthModifier)
                .padding(16.dp),
            contentScale = ContentScale.Fit
        )
    }
}
