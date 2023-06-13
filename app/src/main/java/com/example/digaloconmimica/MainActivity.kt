package com.example.digaloconmimica

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private val movies = mutableListOf(
        "Titanic",
        "El Rey León",
        "La La Land",
        "Jurassic Park",
        "El Padrino",
        "Matrix",
        "El caballero de la noche",
        "Interstellar",
        "El gran showman",
        "Volver al futuro",
        "El exorcista",
        "Pulp Fiction",
        "Psicosis",
        "Cadena perpetua",
        "Gladiador",
        "Casablanca",
        "V de Vendetta",
        "Blade Runner",
        "Rocky",
        "Forrest Gump",
        "El silencio de los corderos",
        "El resplandor",
        "El club de la pelea",
        "El origen",
        "Memento",
        "Sexto sentido",
        "El gran dictador",
        "El ciudadano Kane",
        "Taxi Driver",
        "Amelie",
        "La vida es bella",
        "El curioso caso de Benjamin Button",
        "El show de Truman",
        "La naranja mecánica",
        "El bueno, el malo y el feo",
        "Náufrago",
        "El club de los poetas muertos",
        "El laberinto del fauno",
        "El pianista",
        "El exorcismo de Emily Rose",
        "Kill Bill",
        "El quinto elemento",
        "El lobo de Wall Street",
        "La red social",
        "La milla verde",
        "La lista de Schindler",
        "El hombre araña",
        "El diario de Noa",
        "El ilusionista",
        "El golpe",
        "Django sin cadenas",
        "La forma del agua",
        "La guerra de las galaxias",
        "El gran hotel Budapest",
        "La isla siniestra",
        "El gran Gatsby",
        "El aviador",
        "La red",
        "El juego de la fortuna",
        "El club de la lucha",
        "La máscara",
        "El luchador",
        "La boda de mi mejor amigo",
        "El espantatiburones",
        "El vengador del futuro",
        "El diario de Bridget Jones",
        "El rey arturo",
        "La última cena",
        "El efecto mariposa",
        "El guardaespaldas",
        "La terminal",
        "El ilusionista",
        "El Grinch",
        "El perfume",
        "El día de mañana",
        "El protegido",
        "La vida de los otros",
        "El castigador",
        "El día después de mañana",
        "El gran héroe americano",
        "La joya de la familia",
        "El último mohicano",
        "El paciente inglés",
        "El club de los suicidas",
        "La fuente de la vida",
        "El secreto de sus ojos",
        "El último samurái",
        "La ciudad de los niños perdidos",
        "El amor en los tiempos del cólera",
        "El perfecto asesino",
        "La ventana secreta",
        "Star Wars",
        "El corazón del ángel",
    )

    private lateinit var timerTextView: TextView
    private lateinit var generateMovieButton: Button
    private lateinit var stopTimerButton: Button
    private lateinit var settingsLayout: LinearLayout
    private lateinit var settingsShow: TextView
    private lateinit var redScoreTextView: TextView
    private lateinit var blueScoreTextView: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private val defaultBackgroundColor = Color.BLACK // Replace with your desired default background color

    var timerStarted = false
    var hiddenMovieTitle = ""

    private var countDownTimer: CountDownTimer? = null
    private var selectedTeam = ""
    private var redTeamScore = 0
    private var blueTeamScore = 0
    private var colorAnimator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_logo)


        timerTextView = findViewById(R.id.timer_text_view)
        generateMovieButton = findViewById(R.id.generate_movie_button)
        stopTimerButton = findViewById(R.id.stop_timer_button)
        settingsLayout = findViewById(R.id.team_radio_group)
        redScoreTextView = findViewById(R.id.red_score_text_view)
        blueScoreTextView = findViewById(R.id.blue_score_text_view)

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound)
        mediaPlayer.isLooping = false
        val movieTitleTextView: TextView = findViewById(R.id.movie_title_text_view)
        val timerDurationEditText: EditText = findViewById(R.id.timer_duration_edit_text)
        val saveSettingsButton: Button = findViewById(R.id.save_settings_button)
        val settingsIcon: ImageView = findViewById(R.id.settings_icon)
        settingsLayout = findViewById(R.id.settings_layout)
        settingsShow = findViewById(R.id.settings_show)

        generateMovieButton.setOnClickListener {
            val randomMovie = getRandomMovie(movies)
            movieTitleTextView.text = randomMovie
            movies.remove(randomMovie)
            countDownTimer?.cancel()
            settingsLayout.visibility = View.GONE
            generateMovieButton.visibility = View.GONE
            stopTimerButton.visibility = View.VISIBLE
            settingsShow.visibility = View.VISIBLE
            countDownTimer = startTimer(timerTextView)
        }

        movieTitleTextView.setOnClickListener {


            if (movieTitleTextView.text == "Oculta") {
                movieTitleTextView.text = hiddenMovieTitle
            } else {
                hiddenMovieTitle = movieTitleTextView.text.toString()
                movieTitleTextView.text = "Oculta"
            }
        }

        stopTimerButton.setOnClickListener {
            stopTimer()
        }

        saveSettingsButton.setOnClickListener {
            settingsLayout.visibility = View.GONE
            settingsShow.visibility = View.VISIBLE
            settingsIcon.visibility = View.VISIBLE
        }

        settingsIcon.setOnClickListener {
            settingsLayout.visibility = View.VISIBLE
            settingsShow.visibility = View.GONE
        }

        settingsShow.setOnClickListener {
            settingsLayout.visibility = View.VISIBLE
            settingsShow.visibility = View.GONE
        }

        updateSelectedTeam()

    }

    fun getRandomMovie(movies: MutableList<String>): String {
        if (movies.isEmpty()) {
            return "¡Has jugado todas las películas, reinicia la app!"
        }
        val randomIndex = (0 until movies.size).random()
        return movies[randomIndex]
    }

    private fun startTimer(timerTextView: TextView): CountDownTimer {
        val durationInSeconds = getTimerDuration()
        val durationInMillis = durationInSeconds * 1000
        val backgroundView: View = findViewById(R.id.background_view)

        val colorFrom = Color.GREEN
        val colorTo = Color.RED

        colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimator?.duration = durationInMillis
        colorAnimator?.addUpdateListener { valueAnimator ->
            val color = valueAnimator.animatedValue as Int
            backgroundView.setBackgroundColor(color)
        }


        return object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
                val remainingSeconds = (millisUntilFinished / 1000).toInt()
                if (remainingSeconds == 10) {
                    mediaPlayer.start()
                }
            }

            override fun onFinish() {
                timerTextView.text = "¡TIEMPO!"
                stopTimerButton.visibility = View.GONE
                generateMovieButton.visibility = View.VISIBLE
                colorAnimator?.cancel()
                val backgroundView: View? = findViewById(R.id.background_view)
                if (backgroundView != null) {
                    Log.d("MainActivity", "background_view found") // Add this line
                    backgroundView.setBackgroundColor(Color.parseColor("#80000000"))
                }
                showResultDialog()
            }
        }.apply {
            start()
            colorAnimator?.start()
        }
    }


    private fun stopTimer() {
        Log.d("MainActivity", "stopTimer called")
        countDownTimer?.cancel()
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(this, Uri.parse("android.resource://$packageName/${R.raw.alarm_sound}"))
        mediaPlayer.prepare()
        timerTextView.text = "00:00"
        generateMovieButton.visibility = View.VISIBLE
        stopTimerButton.visibility = View.GONE
        colorAnimator?.cancel()

        val backgroundView: View? = findViewById(R.id.background_view)
        if (backgroundView != null) {
            Log.d("MainActivity", "background_view found") // Add this line
            backgroundView.setBackgroundColor(Color.parseColor("#80000000"))
        } else {
            Log.d("MainActivity", "background_view not found") // Add this line
        }

        showResultDialog()
    }


    private fun showResultDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Resultado")
        builder.setMessage("Adivinaron correctamente?")
        builder.setPositiveButton("Correcto") { _, _ ->
            // Update score for the correct team
            if (selectedTeam == "Rojo") {
                redTeamScore++
            } else if (selectedTeam == "Azul") {
                blueTeamScore++
            }
            updateScore()
        }
        builder.setNegativeButton("Incorrecto") { _, _ ->
            // Do nothing, score stays the same
            showResultDialogDos()
        }
        builder.show()
    }

    private fun showResultDialogDos() {
        val builderB = AlertDialog.Builder(this)
        builderB.setTitle("Equipo Contrario")
        builderB.setMessage("¿Adivinaron correctamente?")
        builderB.setNegativeButton("Incorrecto") { _, _ ->
            if (selectedTeam == "Rojo") {
                blueTeamScore--

            } else if (selectedTeam == "Azul") {
                redTeamScore--
            }
            updateScore()
        }
        builderB.setPositiveButton("Correcto") { _, _ ->
            // Update score for the correct team
            if (selectedTeam == "Rojo") {
                blueTeamScore++
            } else if (selectedTeam == "Azul") {
                redTeamScore++
            }
            updateScore()

        }

        builderB.setNeutralButton("Paso") { _, _ ->
            // No hace nada y cierra el cuadro de diálogo
        }

        builderB.setCancelable(false)
        builderB.show()
    }

    private fun updateSelectedTeam() {
        val redRadioButton: RadioButton = findViewById(R.id.red_radio_button)
        val blueRadioButton: RadioButton = findViewById(R.id.blue_radio_button)

        redRadioButton.setOnClickListener {
            selectedTeam = "Rojo"
        }

        blueRadioButton.setOnClickListener {
            selectedTeam = "Azul"
        }
    }

    private fun updateScore() {
        redScoreTextView.text = "Equipo Rojo: $redTeamScore"
        blueScoreTextView.text = "Equipo Azul: $blueTeamScore"
    }

    private fun getTimerDuration(): Long {
        val timerDurationEditText: EditText = findViewById(R.id.timer_duration_edit_text)
        val durationInSeconds = timerDurationEditText.text.toString().toLongOrNull() ?: 60
        return durationInSeconds
    }
}

