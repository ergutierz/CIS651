package com.example.cis651syracuse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cis651syracuse.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.buttonEasy.setOnClickListener {
            startActivity(GameActivity.newIntent(this@MainActivity, GameLevel.EASY))
        }
        binding.buttonMedium.setOnClickListener {
            startActivity(GameActivity.newIntent(this@MainActivity, GameLevel.MEDIUM))
        }
        binding.buttonDifficult.setOnClickListener {
            startActivity(GameActivity.newIntent(this@MainActivity, GameLevel.DIFFICULT))
        }
    }
}