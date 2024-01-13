package com.example.cis651syracuse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cis651syracuse.databinding.ActivityScoreBinding

class ScoreActivity : AppCompatActivity(){

    private val binding: ActivityScoreBinding by lazy {
        ActivityScoreBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val score = intent.getIntExtra(EXTRA_SCORE, 0)
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