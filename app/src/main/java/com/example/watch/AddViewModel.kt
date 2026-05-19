package com.example.watch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watch.data.Movie
import com.example.watch.MovieRepository
import kotlinx.coroutines.launch

class AddViewModel(private val repository: MovieRepository) : ViewModel() {

    fun saveMovie(title: String, year: String, poster: String, imdbId: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            val movie = Movie(
                imdbID = imdbId,
                title = title,
                year = year,
                poster = poster
            )
            repository.saveMovie(movie)
            onComplete()
        }
    }
}