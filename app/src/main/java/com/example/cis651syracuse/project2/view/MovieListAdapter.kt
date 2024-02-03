package com.example.cis651syracuse.project2.view

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cis651syracuse.project2.model.Movie
import com.example.cis651syracuse.project2.view.components.MovieCard
import com.example.cis651syracuse.project2.viewmodel.MovieListViewModel

class MovieListAdapter(
    items: List<Movie>,
    private val onAction: (action: MovieListViewModel.Action) -> Unit
) : RecyclerView.Adapter<MovieListAdapter.ComposeViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    init {
        differ.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComposeViewHolder {
        val composeView = ComposeView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        return ComposeViewHolder(composeView)
    }

    override fun onBindViewHolder(holder: ComposeViewHolder, position: Int) {
        holder.composeView.setContent {
            MovieCard(movie = differ.currentList[position], onAction = onAction)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(newItems: List<Movie>) {
        differ.submitList(newItems)
    }

    class ComposeViewHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)
}
