package com.example.digaloconmimica

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val movies = listOf("Movie 1", "Movie 2", "Movie 3", "Movie 4")
    private var countDownTimer: CountDownTimer? = null
    private lateinit var settingsLayout: LinearLayout
    private lateinit var stopTimerButton: Button
    private lateinit var generateMovieButton: Button
    private lateinit var timerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movieTitleTextView: TextView = findViewById(R.id.movie_title_text_view)
        generateMovieButton = findViewById(R.id.generate_movie_button)
        val timerDurationEditText: EditText = findViewById(R.id.timer_duration_edit_text)
        val saveSettingsButton: Button = findViewById(R.id.save_settings_button)
        timerTextView = findViewById(R.id.timer_text_view)
        stopTimerButton = findViewById(R.id.stop_timer_button)
        val settingsIcon: ImageView = findViewById(R.id.settings_icon)
        settingsLayout = findViewById(R.id.settings_layout)

        generateMovieButton.setOnClickListener {
            val randomMovie = getRandomMovie(movies)
            movieTitleTextView.text = randomMovie
            countDownTimer?.cancel()
            countDownTimer = startTimer(timerTextView)
            settingsLayout.visibility = View.GONE
            generateMovieButton.visibility = View.GONE
            stopTimerButton.visibility = View.VISIBLE
        }

        saveSettingsButton.setOnClickListener {
            settingsLayout.visibility = View.GONE
        }

        settingsIcon.setOnClickListener {
            settingsLayout.visibility = View.VISIBLE
        }

        stopTimerButton.setOnClickListener {
            countDownTimer?.cancel()
            timerTextView.text = "00:00"
            stopTimerButton.visibility = View.GONE
            generateMovieButton.visibility = View.VISIBLE
        }
    }

    private fun getRandomMovie(movies: List<String>): String {
        val randomIndex = (movies.indices).random()
        return movies[randomIndex]
    }

    private fun getTimerDuration(): Long {
        val timerDurationEditText: EditText = findViewById(R.id.timer_duration_edit_text)
        val durationInSeconds = timerDurationEditText.text.toString().toLongOrNull() ?: 60
        return durationInSeconds
    }


    private fun startTimer(
        timerTextView: TextView
    ): CountDownTimer {
        val durationInSeconds = getTimerDuration()
        val durationInMillis = durationInSeconds * 1000
        return object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timerTextView.text = "¡TIEMPO!"
                stopTimerButton.visibility = View.GONE
                generateMovieButton.visibility = View.VISIBLE
            }
        }.start()
    }
}








