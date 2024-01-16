package com.shubhamtripz.mint

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var artistTextView: TextView
    private lateinit var playPauseButton: Button
    private lateinit var backbtn: ImageView
    private lateinit var musicSeekBar: SeekBar
    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false
    private var isSeeking = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imageView = findViewById(R.id.detailImageView)
        titleTextView = findViewById(R.id.detailTitleTextView)
        artistTextView = findViewById(R.id.detailArtistTextView)
        playPauseButton = findViewById(R.id.playPauseButton)
        musicSeekBar = findViewById(R.id.musicSeekBar)
        backbtn = findViewById(R.id.backbtn)

        // Backbutton handle
        backbtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val title= intent.getStringExtra("title")
        val artist = intent.getStringExtra("artist")
        val imageUrl = intent.getStringExtra("imageUrl")
        val musicUrl = intent.getStringExtra("musicUrl")

        titleTextView.text = title
        artistTextView.text = artist

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.musicholder)
            .into(imageView)

        mediaPlayer = MediaPlayer()

        // Set up SeekBar change listener
        musicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val newPosition = progress * 1000 // progress in milliseconds
                    mediaPlayer.seekTo(newPosition)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isSeeking = false
            }
        })

        // Set up periodic update of SeekBar progress
        val updateHandler = android.os.Handler(mainLooper)
        updateHandler.postDelayed(object : Runnable {
            override fun run() {
                if (!isSeeking) {
                    musicSeekBar.progress = mediaPlayer.currentPosition / 1000 // progress in seconds
                }
                updateHandler.postDelayed(this, 1000) // update every second
            }
        }, 1000)

        playPauseButton.setOnClickListener {
            if (isPlaying) {
                mediaPlayer.pause()
                playPauseButton.text = "Play"
            } else {
                mediaPlayer.reset()
                mediaPlayer.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                mediaPlayer.setDataSource(this, Uri.parse(musicUrl))
                mediaPlayer.prepare()
                mediaPlayer.start()
                playPauseButton.text = "Pause"
            }
            isPlaying = !isPlaying
        }


        // Create onBackPressedCallback
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@DetailActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Add onBackPressedCallback to onBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}