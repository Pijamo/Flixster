package com.example.flixster

import android.media.Rating
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.flixster.databinding.ActivityDetailBinding
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers


private const val YOUTUBE_API_KEY = "AIzaSyBrrrys_HUTPXZUbs8KXY_6AkmATC-CnN8"
private const val TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val TAG = "DetailActivity"

class DetailActivity : YouTubeBaseActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val ytPLayerView = binding.player

        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie

        binding.details = movie

        Log.i(TAG, "Movie is $movie")

        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieId), object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess")
                val results = json.jsonObject.getJSONArray("results")

                if (results.length() == 0){
                    Log.w(TAG, "No movie trailers found")
                    return
                }
                val movieTrailerJson = results.getJSONObject(0)
                val youtubeKey = movieTrailerJson.getString("key")

                //play youtube
                initializeYoutube(youtubeKey, ytPLayerView, binding.rbVoteAverage)
            }
        })
    }

    private fun initializeYoutube(youtubeKey: String, ytPLayerView: YouTubePlayerView, ratingBar: RatingBar) {

        ytPLayerView.initialize(YOUTUBE_API_KEY, object: YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i(TAG, "onInitializationSuccess")
                if(ratingBar.rating > 5){
                    player?.loadVideo(youtubeKey)
                } else {
                    player?.cueVideo(youtubeKey)
                }
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.i(TAG, "onInitializationFailure")
            }
        })
    }
}