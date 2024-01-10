package com.shubhamtripz.mint

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SongAdapter(private val context: Context, private val songList: List<Song>) :
    RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val artistTextView: TextView = itemView.findViewById(R.id.artistTextView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]

        // Check for null values and set default text if null
        holder.titleTextView.text = song.Title
        holder.artistTextView.text = song.Artist

        // Check for a valid image URL before loading
        if (!song.Image.isNullOrBlank()) {
            Glide.with(context)
                .load(song.Image)
                .placeholder(R.drawable.musicholder)
                .into(holder.imageView)
        } else {
            // Load a placeholder image if the URL is null or blank
            holder.imageView.setImageResource(R.drawable.musicholder)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("title", song.Title ?: "")
            intent.putExtra("artist", song.Artist ?: "")
            intent.putExtra("imageUrl", song.Image ?: "")
            intent.putExtra("musicUrl", song.Link ?: "")
            context.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return songList.size
    }


}