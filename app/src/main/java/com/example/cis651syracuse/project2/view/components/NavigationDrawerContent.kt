package com.example.cis651syracuse.project2.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cis651syracuse.project2.util.Screen
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawerContent(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    onNavItemClick: (screen: Screen) -> Unit
) {
    val typography = Typography(
        h5 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color(0xFFFFD700)
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color.White
        )
    )
    Box(modifier = modifier.background(Color.Black)) {
        Column(
            modifier = Modifier
                .matchParentSize()
                .padding(end = 1.dp)
        ) {
            Column {
                Text(text = "Cinema Chronicles", style = typography.h5)
                ThemedSeparator()
                DrawerItem(Screen.Dashboard, scaffoldState, onNavItemClick, typography)
                DrawerItem(Screen.Movies, scaffoldState, onNavItemClick, typography)
                DrawerItem(Screen.RandomMovie, scaffoldState, onNavItemClick, typography)
            }
        }
        Spacer(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .width(1.dp)
                .fillMaxHeight()
                .background(Color(0xFFFFD700))
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerItem(
    screen: Screen,
    scaffoldState: ScaffoldState,
    onNavItemClick: (screen: Screen) -> Unit,
    typography: Typography
) {
    val coroutineScope = rememberCoroutineScope()
    ListItem(
        text = { Text(screen.title, style = typography.body1) },
        modifier = Modifier.clickable {
            onNavItemClick(screen)
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        }
    )
}
