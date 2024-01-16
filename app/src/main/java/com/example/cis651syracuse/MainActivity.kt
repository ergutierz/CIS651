package com.example.cis651syracuse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        startActivity(GameActivity.newIntent(this@MainActivity, GameLevel.EASY))
                    }) {
                    Text(text = stringResource(id = R.string.easy))
                }
                Button(
                    modifier = Modifier
                    .fillMaxWidth(),
                    onClick = {
                        startActivity(GameActivity.newIntent(this@MainActivity, GameLevel.MEDIUM))
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
                                GameLevel.DIFFICULT
                            )
                        )
                    }) {
                    Text(text = stringResource(id = R.string.difficult))
                }
            }
        }
    }
}