package com.example.cis651syracuse

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    fun onAction(action: Action) {
        when (action) {
            is Action.InitializeGame -> initializeGame(action.difficulty)
            is Action.FlipCard -> flipCard(action.index)
        }
    }

    private fun initializeGame(difficulty: GameDifficulty) {
        val (rows, cols) = when (difficulty) {
            GameDifficulty.EASY -> 3 to 3
            GameDifficulty.MEDIUM -> 4 to 4
            GameDifficulty.DIFFICULT -> 5 to 5
        }
        val cards = createDrawableList(rows * cols, cardImages)
        _state.update { oldState ->
            oldState.copy(
                grid = cards.map { imageRes ->
                    CardState(
                        imageRes = imageRes
                    )
                },
                columns = cols
            )
        }
    }

    private fun flipCard(index: Int) {
        viewModelScope.launch {
            val currentState = _state.value
            val currentCard = currentState.grid[index]
            if (currentCard.isFlipped || currentCard.isRemoved) return@launch

            // Flip the selected card
            val newGrid = currentState.grid.toMutableList().apply {
                this[index] = currentCard.copy(isFlipped = true)
            }
            _state.value = currentState.copy(grid = newGrid, score = currentState.score + 1)

            currentState.lastFlippedIndex?.let { lastFlippedIndex ->
                val lastFlippedCard = newGrid[lastFlippedIndex]
                if (lastFlippedCard.imageRes == currentCard.imageRes && lastFlippedIndex != index) {
                    // Match found, mark both cards as removed
                    newGrid[index] = currentCard.copy(isRemoved = true)
                    newGrid[lastFlippedIndex] = lastFlippedCard.copy(isRemoved = true)
                    delay(500) // Short delay before removing cards
                } else {
                    // No match, flip them back after a delay
                    delay(1000)
                    newGrid[index] = currentCard.copy(isFlipped = false)
                    newGrid[lastFlippedIndex] = lastFlippedCard.copy(isFlipped = false)
                }
                _state.value = currentState.copy(grid = newGrid, lastFlippedIndex = null)
            } ?: run {
                // Update last flipped index if no previous card to compare
                _state.value = currentState.copy(lastFlippedIndex = index)
            }

            if (newGrid.all { it.isRemoved }) {
                _event.tryEmit(Event.NavigateToScoreDisplay(currentState.score))
            }
        }
    }

    private fun createDrawableList(size: Int, drawables: List<Int>): List<Int> {
        val numberOfUniqueCards = (size * 0.25).toInt()
        val uniqueCards = drawables.shuffled().take(numberOfUniqueCards)
        val remainingCards =
            (1..size - numberOfUniqueCards).flatMap { uniqueCards.shuffled().take(2) }
        return (uniqueCards + remainingCards).shuffled().take(size)
    }

    data class GameState(
        val grid: List<CardState> = emptyList(),
        val score: Int = 0,
        val columns: Int = 0,
        val lastFlippedIndex: Int? = null
    )

    data class CardState(
        @DrawableRes
        val imageRes: Int,
        val isFlipped: Boolean = false,
        val isRemoved: Boolean = false
    )

    sealed class Action {
        data class FlipCard(val index: Int) : Action()
        data class InitializeGame(val difficulty: GameDifficulty) : Action()
    }

    sealed class Event {
        data class NavigateToScoreDisplay(val score: Int) : Event()
    }

    companion object {
        private val cardImages = listOf(
            R.drawable.img1, R.drawable.img2, R.drawable.img3,
            R.drawable.img4, R.drawable.img5, R.drawable.img6,
            R.drawable.img7, R.drawable.img8, R.drawable.img9,
            R.drawable.img10
        )
    }
}
