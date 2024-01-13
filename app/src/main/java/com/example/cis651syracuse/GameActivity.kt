package com.example.cis651syracuse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cis651syracuse.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    private val binding: ActivityGameBinding by lazy {
        ActivityGameBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupGame()
    }

    private fun setupGame() {
        val clubCards = listOf(
            R.drawable.clubs_2, R.drawable.clubs_3, R.drawable.clubs_4,
            R.drawable.clubs_5, R.drawable.clubs_6, R.drawable.clubs_7,
            R.drawable.clubs_8, R.drawable.clubs_9, R.drawable.clubs_10,
            R.drawable.clubs_ace, R.drawable.clubs_jack,
            R.drawable.clubs_queen, R.drawable.clubs_king
        )
        val difficulty = intent.getStringExtra("DIFFICULTY_LEVEL") ?: "easy"
        val (rows, cols) = when (difficulty) {
            "medium" -> 4 to 4
            "difficult" -> 5 to 5
            else -> 3 to 3
        }
        val drawableList = createDrawableList(rows * cols, clubCards)
        binding.recyclerView.apply {
            setLayoutManager(GridLayoutManager(this@GameActivity, cols))
            setAdapter(GameCardAdapter(drawableList))
        }
    }

    private fun createDrawableList(size: Int, drawables: List<Int>): List<Int> {
        val numberOfUniqueCards = (size * 0.25).toInt()
        val uniqueCards = drawables.shuffled().take(numberOfUniqueCards)
        val remainingCards = (1..size-numberOfUniqueCards).flatMap { uniqueCards.shuffled().take(2) }
        return (uniqueCards + remainingCards).shuffled().take(size)
    }

    companion object {
        private const val EXTRA_GAME_LEVEL = "com.example.cis651syracuse.GAME_LEVEL"

        fun newIntent(context: Context, gameLevel: GameLevel): Intent {
            return Intent(context, GameActivity::class.java).apply {
                putExtra(EXTRA_GAME_LEVEL, gameLevel.ordinal)
            }
        }
    }
}