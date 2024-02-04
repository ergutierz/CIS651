package com.example.cis651syracuse.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun adaptiveTypography(maxWidth: Dp): Typography {
    return Typography(
        h4 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = if (maxWidth < 600.dp) 24.sp else 30.sp,
            color = Color(0xFFFFD700)
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = if (maxWidth < 600.dp) 14.sp else 16.sp,
            color = Color.White
        ),
        subtitle1 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Light,
            fontSize = if (maxWidth < 600.dp) 12.sp else 14.sp,
            color = Color(0xFFCCCCCC)
        )
    )
}
