package com.example.watch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watch.data.MovieApiItem
import com.example.watch.MovieRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: MovieRepository) : ViewModel() {

    var movies by mutableStateOf<List<MovieApiItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun search(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = repository.searchMovies(query)
                if (response.Response == "True") {
                    movies = response.Search ?: emptyList()
                } else {
                    errorMessage = "Фильмы не найдены"
                    movies = emptyList()
                }
            } catch (e: Exception) {
                errorMessage = "Ошибка сети: ${e.localizedMessage}"
                movies = emptyList()
            } finally {
                isLoading = false
            }
        }
    }
}