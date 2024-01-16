package com.example.cis651syracuse

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        modifier = Modifier
                            .fillMaxWidth(),
                        title = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(id = R.string.title_activity_options),
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                },
                content = { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center

                    ) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                startActivity(
                                    GameActivity.newIntent(
                                        this@MainActivity,
                                        GameDifficulty.EASY
                                    )
                                )
                            }) {
                            Text(text = stringResource(id = R.string.easy))
                        }
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                startActivity(
                                    GameActivity.newIntent(
                                        this@MainActivity,
                                        GameDifficulty.MEDIUM
                                    )
                                )
                            }) {
                            Text(text = stringResource(id = R.string.medium))
                        }
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                startActivity(
                                    GameActivity.newIntent(
                                        this@MainActivity,
                                        GameDifficulty.DIFFICULT
                                    )
                                )
                            }) {
                            Text(text = stringResource(id = R.string.difficult))
                        }
                    }
                }
            )
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}