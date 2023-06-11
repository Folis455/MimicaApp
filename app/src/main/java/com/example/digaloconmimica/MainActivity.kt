package com.example.digaloconmimica

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val movies = listOf("Movie 1", "Movie 2", "Movie 3", "Movie 4")
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movieTitleTextView: TextView = findViewById(R.id.movie_title_text_view)
        val generateMovieButton: Button = findViewById(R.id.generate_movie_button)
        val timerDurationEditText: EditText = findViewById(R.id.timer_duration_edit_text)
        val timerTextView: TextView = findViewById(R.id.timer_text_view)
        val stopTimerButton: Button = findViewById(R.id.stop_timer_button)

        generateMovieButton.setOnClickListener {
            val randomMovie = getRandomMovie(movies)
            movieTitleTextView.text = randomMovie
            val durationInSeconds = timerDurationEditText.text.toString().toLongOrNull()
            if (durationInSeconds != null) {
                val durationInMillis = durationInSeconds * 1000
                countDownTimer?.cancel()
                countDownTimer = startTimer(durationInMillis, timerTextView)
            } else {
                Toast.makeText(this, "Please enter a valid time", Toast.LENGTH_SHORT).show()
            }
        }

        stopTimerButton.setOnClickListener {
            countDownTimer?.cancel()
            timerTextView.text = "Timer stopped"
        }
    }

    private fun getRandomMovie(movies: List<String>): String {
        val randomIndex = (movies.indices).random()
        return movies[randomIndex]
    }

    private fun startTimer(
        durationInMillis: Long,
        timerTextView: TextView
    ): CountDownTimer {
        return object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timerTextView.text = "Time's up!"
            }
        }.start()
    }
}





