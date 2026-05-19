package com.example.watch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.watch.data.*
import com.example.watch.ui.theme.WatchAppTheme

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val query = intent.getStringExtra("QUERY") ?: ""

        val db = MovieDatabase.getDatabase(this)
        val repository = MovieRepository(db.movieDao(), RetrofitClient.api)
        val viewModel = SearchViewModel(repository)

        setContent {
            WatchAppTheme {
                LaunchedEffect(query) {
                    viewModel.search(query)
                }

                SearchScreen(query, viewModel) { selectedMovie ->
                    val resultIntent = Intent().apply {
                        putExtra("title", selectedMovie.Title)
                        putExtra("year", selectedMovie.Year)
                        putExtra("poster", selectedMovie.Poster)
                        putExtra("imdb", selectedMovie.imdbID)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(query: String, viewModel: SearchViewModel, onMovieSelected: (MovieApiItem) -> Unit) {

    val movies = viewModel.movies
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search: $query") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (errorMessage != null) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            } else if (movies.isEmpty()) {
                Text("No results found")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(movies) { movie ->
                        MovieSearchItem(movie) { onMovieSelected(movie) }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieSearchItem(movie: MovieApiItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = movie.Poster,
                contentDescription = null,
                modifier = Modifier.size(60.dp, 90.dp)
            )
            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(text = movie.Title, style = MaterialTheme.typography.titleMedium)
                Text(text = movie.Year, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}