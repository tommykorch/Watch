package com.example.watch

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watch.data.*
import com.example.watch.databinding.ActivitySearchMovieBinding
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rcView.layoutManager = LinearLayoutManager(this)
        val query = intent.getStringExtra("QUERY") ?: ""

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(OmdbResponse::class.java)

        lifecycleScope.launch {
            try {
                val response = api.searchMovies(query)
                if (response.Search != null) {
                    binding.rcView.adapter = SearchAdapter(response.Search) { movie ->
                        val resultIntent = Intent().apply {
                            putExtra("title", movie.Title)
                            putExtra("year", movie.Year)
                            putExtra("poster", movie.Poster)
                            putExtra("imdb", movie.imdbID)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}