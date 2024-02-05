package com.example.cis651syracuse.project2.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cis651syracuse.R
import com.example.cis651syracuse.core.DeviceUtils
import com.example.cis651syracuse.project2.view.components.ThemedSeparator

@Composable
fun MovieAboutScreen(modifier: Modifier = Modifier) {
    val typography = Typography(
        h4 = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 30.sp, color = Color(0xFFFFD700)),
        body1 = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.White)
    )

    Surface(color = Color.Black) {
        Column(modifier = modifier) {
            Text(stringResource(id = R.string.welcome_to_cinema_chronicles), style = typography.h4)
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(id = R.string.dive_into_the_heart_of_storyte), style = typography.body1)

            ThemedSeparator()
            ThemedIcon(painter = painterResource(id = R.drawable.classic_theatre), contentDescription = stringResource(
                id = R.string.the_vision
            ))

            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(id = R.string.the_vision), style = typography.h4)
            Text(stringResource(id = R.string.created_by_erik_a_software_eng), style = typography.body1)
        }
    }
}

@Composable
fun ThemedIcon(painter: Painter, contentDescription: String?) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(550.dp.takeIf { DeviceUtils.isTablet(LocalContext.current) } ?: 200.dp)
    )
}
