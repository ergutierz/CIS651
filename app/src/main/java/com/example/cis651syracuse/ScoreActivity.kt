package com.example.cis651syracuse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

class ScoreActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val score = intent.getIntExtra(EXTRA_SCORE, 0)
        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        modifier = Modifier
                            .fillMaxWidth(),
                        title = { Text(text = stringResource(id = R.string.title_activity_score)) },
                        navigationIcon = {
                            IconButton(onClick = {
                                startActivity(MainActivity.newIntent(this@ScoreActivity))
                                finish()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                },
                content = { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.final_score, score),
                            style = MaterialTheme.typography.headlineLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        }
    }

    companion object {
        private const val EXTRA_SCORE = "com.example.cis651syracuse.SCORE"

        fun newIntent(context: Context, score: Int): Intent {
            return Intent(context, ScoreActivity::class.java).apply {
                putExtra(EXTRA_SCORE, score)
            }
        }
    }
}