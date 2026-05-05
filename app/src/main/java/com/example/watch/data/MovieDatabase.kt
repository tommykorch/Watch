package com.example.watch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile private var instance: MovieDatabase? = null
        fun getDatabase(context: Context): MovieDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java, "watch_db"
                ).build().also { instance = it }
            }
    }
}