package com.example.digaloconmimica

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val movies = listOf("Movie 1", "Movie 2", "Movie 3", "Movie 4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movieTitleTextView: TextView = findViewById(R.id.movie_title_text_view)
        val generateMovieButton: Button = findViewById(R.id.generate_movie_button)
        val timerDurationEditText: EditText = findViewById(R.id.timer_duration_edit_text)
        val startTimerButton: Button = findViewById(R.id.start_timer_button)
        val timerTextView: TextView = findViewById(R.id.timer_text_view)

        generateMovieButton.setOnClickListener {
            val randomMovie = getRandomMovie(movies)
            movieTitleTextView.text = randomMovie
        }

        startTimerButton.setOnClickListener {
            val durationInSeconds = timerDurationEditText.text.toString().toLong()
            val durationInMillis = durationInSeconds * 1000
            startTimer(durationInMillis, timerTextView)
        }
    }

    private fun getRandomMovie(movies: List<String>): String {
        val randomIndex = (movies.indices).random()
        return movies[randomIndex]
    }

    private fun startTimer(durationInMillis: Long, timerTextView: TextView) {
        object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                timerTextView.text = "Time's up!"
            }
        }.start()
    }
}
