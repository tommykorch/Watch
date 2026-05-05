package com.example.watch.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Query("DELETE FROM movies WHERE isSelectedForDelete = 1")
    suspend fun deleteSelected()

    @Query("UPDATE movies SET isSelectedForDelete = :selected WHERE id = :id")
    suspend fun updateSelection(id: Int, selected: Boolean)
}