package com.example.watch.data
import retrofit2.http.GET
import retrofit2.http.Query

data class MovieSearchResponse(val Search: List<MovieApiItem>?, val Response: String)
data class MovieApiItem(val Title: String, val Year: String, val imdbID: String, val Poster: String, val Type: String)

interface OmdbResponse {
    @GET("/")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("apikey") apiKey: String = "795d266"
    ): MovieSearchResponse
}