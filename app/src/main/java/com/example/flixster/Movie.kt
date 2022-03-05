package com.example.flixster


import android.content.res.Configuration
import android.content.res.Resources
import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.json.JSONArray

@Parcelize
data class Movie (
    val movieId: Int,
    private val posterPath: String,
    val title: String,
    val overview: String,
    var voteAverage: Float,
) : Parcelable {
    @IgnoredOnParcel
    val posterImageUrl = "https://image.tmdb.org/t/p/w342/$posterPath"
    val backgroundImageUrl = "https://image.tmdb.org/t/p/w300/$posterPath"

    companion object {
        fun fromJasonArray(movieJsonArray: JSONArray): List<Movie> {
            val movies = mutableListOf<Movie>()

            for (i in 0 until movieJsonArray.length()){
                val movieJson = movieJsonArray.getJSONObject(i)

                movies.add(
                    Movie(
                        movieJson.getInt("id"),
                        movieJson.getString("poster_path"),
                        movieJson.getString("title"),
                        movieJson.getString("overview"),
                        movieJson.getDouble("vote_average").toFloat(),
                    )
                )
            }
            return movies
        }
    }
}
