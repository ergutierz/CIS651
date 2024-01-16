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
            val currentCard = _state.value.grid[index]

            if (currentCard.isFlipped || currentCard.isRemoved) return@launch

            _state.update { oldState ->
                val newGrid = oldState.grid.toMutableList().apply {
                    this[index] = currentCard.copy(isFlipped = true)
                }
                oldState.copy(grid = newGrid, score = oldState.score + 1)
            }

            val lastFlippedIndex = _state.value.lastFlippedIndex
            val allCardsFlipped = _state.value.grid.all { it.isFlipped }
            when {
                allCardsFlipped -> _event.emit(Event.NavigateToScoreDisplay(_state.value.score))
                lastFlippedIndex != null -> processFlippedCards(index, lastFlippedIndex)
                else -> _state.update { it.copy(lastFlippedIndex = index) }
            }
        }
    }

    private suspend fun processFlippedCards(index: Int, lastFlippedIndex: Int) {
        delay(1000)
        _state.update { oldState ->
            val newGrid = oldState.grid.toMutableList()
            val currentCard = newGrid[index]
            val lastFlippedCard = newGrid[lastFlippedIndex]

            if (lastFlippedCard.imageRes == currentCard.imageRes && lastFlippedIndex != index) {
                // Match found, mark both cards as removed
                newGrid[index] = currentCard.copy(isRemoved = true)
                newGrid[lastFlippedIndex] = lastFlippedCard.copy(isRemoved = true)
            } else {
                // No match, flip them back
                newGrid[index] = currentCard.copy(isFlipped = false)
                newGrid[lastFlippedIndex] = lastFlippedCard.copy(isFlipped = false)
            }
            oldState.copy(grid = newGrid, lastFlippedIndex = null)
        }
    }

    private fun createDrawableList(size: Int, drawables: List<Int>): List<Int> {
        val numberOfPairs = size / 2
        val selectedImages = drawables.shuffled().take(numberOfPairs).toList()
        val pairedImages = selectedImages + selectedImages

        // Adjust for odd-sized grids
        val finalCards = if (size % 2 != 0) {
            // Check if there's any drawable not already in selectedImages
            val extraImage = drawables.firstOrNull { it !in selectedImages }
            if (extraImage != null) {
                pairedImages + extraImage
            } else {
                // If no unique drawable is available, use a placeholder or repeat one of the existing drawables
                pairedImages + selectedImages.first()
            }
        } else {
            pairedImages
        }

        // Shuffle the final card list to randomize their distribution
        return finalCards.shuffled()
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
            R.drawable.img10, R.drawable.img11, R.drawable.img12,
        )
    }
}
