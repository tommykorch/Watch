package com.example.watch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.watch.data.MovieApiItem
import com.example.watch.databinding.ListItemMovieBinding

class SearchAdapter(
    private val items: List<MovieApiItem>,
    private val onMovieSelected: (MovieApiItem) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    class SearchViewHolder(val binding: ListItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ListItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            title.text = item.Title
            Year.text = item.Year
            genres.text = "Movie"
            isWatched.visibility = android.view.View.GONE
            Glide.with(root.context).load(item.Poster).into(Poster)

            root.setOnClickListener  {
                onMovieSelected(item)
            }
        }
    }
    override fun getItemCount() = items.size
}