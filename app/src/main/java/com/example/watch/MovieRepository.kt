package com.example.watch

import com.example.watch.data.*

class MovieRepository(private val movieDao: MovieDao, private val api: OmdbResponse) {

    val allMovies = movieDao.getAllMovies()
    suspend fun saveMovie(movie: Movie) = movieDao.insert(movie)

    suspend fun deleteMovie(movie: Movie) = movieDao.delete(movie)

    suspend fun searchMovies(query: String) = api.searchMovies(query)
}