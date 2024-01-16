package com.shubhamtripz.mint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var songAdapter: SongAdapter
    private lateinit var shimmerView: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        songAdapter = SongAdapter(this, emptyList())
        recyclerView.adapter = songAdapter
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        shimmerView = findViewById(R.id.shimmer_layout)

        // Fetch data from API
        fetchData()

        val search_img_btn : ImageView = findViewById(R.id.search_img_btn)

        search_img_btn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

    }

    private fun fetchData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://script.google.com/macros/s/AKfycbylPAlCHZZCQpI_rKOSpze-hNmfh6ndkPhEWeMg83xnL6UF3M-aDsvovur47kBGG6fr7Q/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)
        val call = service.getUsers()

        call.enqueue(object : Callback<List<Song>> {
            override fun onResponse(call: Call<List<Song>>, response: Response<List<Song>>) {
                if (response.isSuccessful) {
                    val songList = response.body()
                    if (songList != null) {
                        val reversedSongList = songList.reversed()
                        songAdapter = SongAdapter(this@MainActivity, reversedSongList)
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                        recyclerView.adapter = songAdapter
                        shimmerView.stopShimmer()
                        shimmerView.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<List<Song>>, t: Throwable) {
                // Handle failure
            }
        })
    }
}