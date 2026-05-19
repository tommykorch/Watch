package com.example.watch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.example.watch.data.*
import com.example.watch.ui.theme.WatchAppTheme
import kotlinx.coroutines.launch
import com.example.watch.AddViewModel
import com.example.watch.MovieRepository

class AddActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = MovieDatabase.getDatabase(this)
        val repository = MovieRepository(db.movieDao(), RetrofitClient.api)
        val viewModel = AddViewModel(repository)

        setContent {
            WatchAppTheme {
                AddScreen(viewModel) {
                    finish()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(viewModel: AddViewModel, onSaveSuccess: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var posterUrl by remember { mutableStateOf("") }
    var imdbId by remember { mutableStateOf("") }

    val searchLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            title = data?.getStringExtra("title") ?: ""
            year = data?.getStringExtra("year") ?: ""
            posterUrl = data?.getStringExtra("poster") ?: ""
            imdbId = data?.getStringExtra("imdb") ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add New Movie") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Movie Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Release Year") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val intent = Intent(context, SearchActivity::class.java).apply {
                        putExtra("QUERY", title)
                    }
                    searchLauncher.launch(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }

            if (posterUrl.isNotEmpty()) {
                Text("Selected Poster:", style = MaterialTheme.typography.labelLarge)
                AsyncImage(
                    model = posterUrl,
                    contentDescription = "Selected Poster",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (title.isNotBlank() && imdbId.isNotBlank()) {
                        scope.launch {
                            val movie = Movie(
                                imdbID = imdbId,
                                title = title,
                                year = year,
                                poster = posterUrl
                            )
                            viewModel.saveMovie(title, year, posterUrl, imdbId) {
                                onSaveSuccess()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = title.isNotBlank() && imdbId.isNotBlank()
            ) {
                Text("Save to Favorites")
            }
        }
    }
}