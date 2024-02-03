package com.example.cis651syracuse.project2.view.components

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment

@Composable
fun FragmentHost(
    fragment: Fragment
) {
    Surface(color = Color.Black) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val viewGroup = FrameLayout(context).apply {
                    id = View.generateViewId()
                }
                val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(viewGroup.id, fragment)
                    .commit()

                viewGroup
            })
    }
}
