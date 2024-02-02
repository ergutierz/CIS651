package com.example.cis651syracuse.project2.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cis651syracuse.project2.util.Screen
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawerContent(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    onNavItemClick: (screen: Screen) -> Unit
) {
    Column(modifier = modifier) {
        Text(text = "Movie Bonanza", style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))
        Divider()
        DrawerItem(Screen.Dashboard, scaffoldState, onNavItemClick)
        DrawerItem(Screen.Movies, scaffoldState, onNavItemClick)
        DrawerItem(Screen.RandomMovie, scaffoldState, onNavItemClick)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerItem(screen: Screen, scaffoldState: ScaffoldState, onNavItemClick: (screen: Screen) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    ListItem(
        text = { Text(screen.title) },
        modifier = Modifier.clickable {
            onNavItemClick(screen)
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        }
    )
}
