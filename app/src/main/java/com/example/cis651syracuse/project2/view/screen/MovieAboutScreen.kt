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
            Text("Welcome to Cinema Chronicles", style = typography.h4)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Dive into the heart of storytelling with Cinema Chronicles, your ultimate portal to the magic of movies. Born from a deep-seated passion for cinematic adventures and the art of filmmaking, this project is a tribute to the timeless allure of the silver screen.", style = typography.body1)

            ThemedSeparator()
            ThemedIcon(painter = painterResource(id = R.drawable.classic_theatre), contentDescription = "Profile Banner")

            Spacer(modifier = Modifier.height(16.dp))
            Text("The Vision", style = typography.h4)
            Text("Created by Erik, a software engineer with a flair for storytelling and a programmer's precision, Cinema Chronicles bridges the gap between technology and the art of cinema. This project is a personal journey through the reels of history, inviting you to join in the exploration of stories that shape our world.", style = typography.body1)
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
