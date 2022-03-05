package com.example.flixster

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flixster.databinding.ActivityDetailBinding
import com.example.flixster.databinding.ItemMovieBinding
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import android.util.Pair as UtilPair


const val MOVIE_EXTRA = "MOVIE_EXTRA"
private const val TAG = "MovieAdapter"

class MovieAdapter(private val context: Context, private val movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>(){



    // Expensive operation: creating a view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    //Cheap: simply binding data to an existing viewholder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder position $position")

        val movie = movies[position]

        holder.binding.movie = movie
        holder.binding.executePendingBindings()
    }

    override fun getItemCount() = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var binding: ItemMovieBinding = ItemMovieBinding.bind(itemView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            //1. Get Notified of the particular movie which was clicked
            val movie = movies[adapterPosition]

            //2. USe the intent system to navigate to the new activity
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(MOVIE_EXTRA, movie)

            val options = ActivityOptions.makeSceneTransitionAnimation((context as Activity)!!,
                UtilPair.create(binding.tvTitle, "titleName"),
                UtilPair.create(binding.tvOverview, "description"))
            context.startActivity(intent, options.toBundle())
        }
    }
}
