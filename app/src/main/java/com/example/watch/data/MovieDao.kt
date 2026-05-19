package com.example.watch.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("DELETE FROM movies WHERE imdbID = :imdbId")
    suspend fun deleteById(imdbId: String)


    @Query("DELETE FROM movies WHERE isSelectedForDelete = 1")
    suspend fun deleteSelected()


    @Query("UPDATE movies SET isSelectedForDelete = :selected WHERE imdbID = :imdbId")
    suspend fun updateSelection(imdbId: String, selected: Boolean)
}