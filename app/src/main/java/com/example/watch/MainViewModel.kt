package com.example.watch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watch.data.*
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MovieRepository) : ViewModel() {
    val movies = repository.allMovies

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            repository.deleteMovie(movie)
        }
    }
}