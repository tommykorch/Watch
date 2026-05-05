package com.example.watch

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watch.data.*
import com.example.watch.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: MovieDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MovieDatabase.getDatabase(this)
        binding.recView.layoutManager = LinearLayoutManager(this)

        db.movieDao().getAllMovies().observe(this) { movies ->
            if (movies.isEmpty()) {
                binding.imageWithoutMovies.visibility = View.VISIBLE
                binding.textWithoutMovies.visibility = View.VISIBLE
                binding.recView.visibility = View.GONE
            } else {
                binding.imageWithoutMovies.visibility = View.GONE
                binding.textWithoutMovies.visibility = View.GONE
                binding.recView.visibility = View.VISIBLE
                binding.recView.adapter = MovieAdapter(movies, db)
            }
        }

        binding.buttonAdd.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        binding.buttonDelete.setOnClickListener {
            lifecycleScope.launch {
                db.movieDao().deleteSelected()
            }
        }
    }
}