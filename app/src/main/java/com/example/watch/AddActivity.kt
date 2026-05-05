package com.example.watch

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.watch.data.*
import com.example.watch.databinding.ActivityAddBinding
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var db: MovieDatabase
    private var selectedImdbId: String = ""
    private var selectedPosterUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = MovieDatabase.getDatabase(this)

        binding.imageButtonSearch.setOnClickListener {
            val query = binding.editTextTitle.text.toString()
            if (query.isNotEmpty()) {
                val intent = Intent(this, SearchActivity::class.java).apply {
                    putExtra("QUERY", query)
                }
                startActivityForResult(intent, 100)
            }
        }

        binding.buttonAddMovie.setOnClickListener {
            val movie = Movie(
                imdbID = selectedImdbId,
                title = binding.editTextTitle.text.toString(),
                year = binding.editTextYear.text.toString(),
                poster = selectedPosterUrl
            )
            lifecycleScope.launch {
                db.movieDao().insert(movie)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            binding.editTextTitle.setText(data?.getStringExtra("title"))
            binding.editTextYear.setText(data?.getStringExtra("year"))
            selectedImdbId = data?.getStringExtra("imdb") ?: ""
            selectedPosterUrl = data?.getStringExtra("poster") ?: ""

            binding.imageViewPoster.visibility = View.VISIBLE
            Glide.with(this).load(selectedPosterUrl).into(binding.imageViewPoster)
        }
    }
}