package com.example.watch.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imdbID: String,
    val title: String,
    val year: String,
    val poster: String,
    var isSelectedForDelete: Boolean = false
)
