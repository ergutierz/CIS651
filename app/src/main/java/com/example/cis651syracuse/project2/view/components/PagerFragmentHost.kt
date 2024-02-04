package com.example.cis651syracuse.project2.view.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment

@Composable
fun PagerFragmentHost(
    fragment: Fragment,
    modifier: Modifier = Modifier
) {
    Surface(color = Color.Black) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                val viewGroup = FrameLayout(context).apply {
                    id = View.generateViewId()
                }
                val actualContext = context.findActivity()
                if (actualContext is AppCompatActivity) {
                    actualContext.supportFragmentManager.beginTransaction()
                        .replace(viewGroup.id, fragment)
                        .commitAllowingStateLoss()
                }

                viewGroup
            }
        )
    }
}

tailrec fun Context?.findActivity(): Activity? = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

