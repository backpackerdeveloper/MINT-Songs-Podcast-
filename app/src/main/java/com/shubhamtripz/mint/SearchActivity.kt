package com.shubhamtripz.mint

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: ImageView
    private lateinit var searchRecyclerView: RecyclerView
    private val apiService = ApiSearchService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        searchRecyclerView = findViewById(R.id.searchRecyclerView)

        searchButton.setOnClickListener {

            val userInput = searchEditText.text.toString()
            performSearch(userInput)
        }

        val back2 = findViewById<ImageView>(R.id.back2btn)

        back2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun performSearch(query: String) {
        val formattedQuery = query.trim()
        val apiCall = apiService.getUsersByTitle(formattedQuery)

        apiCall.enqueue(object : Callback<List<Song>> {
            override fun onResponse(call: Call<List<Song>>, response: Response<List<Song>>) {
                if (response.isSuccessful) {
                    val searchResults = response.body()
                    displaySearchResults(searchResults)
                } else {
                    // Handle API error
                }
            }

            override fun onFailure(call: Call<List<Song>>, t: Throwable) {
                // Handle network failure
            }
        })
    }


    private fun displaySearchResults(searchResults: List<Song>?) {
        if (searchResults != null) {
            val adapter = SearchAdapter(searchResults) { clickedSong ->
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra("title", clickedSong.Title)
                    putExtra("artist", clickedSong.Artist)
                    putExtra("imageUrl", clickedSong.Image)
                    putExtra("musicUrl", clickedSong.Link)
                }
                startActivity(intent)
            }

            searchRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            searchRecyclerView.adapter = adapter
        }
    }
}
