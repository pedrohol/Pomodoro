package com.example.pomodoro

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var mainLayout: ConstraintLayout
    private lateinit var cicleTxt: TextView
    private lateinit var timerTxt: TextView
    private lateinit var startTxt: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var countDownTimer: CountDownTimer
    private var isRunning: Boolean = false
    private var time: Int = 1
    private var timeProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainLayout = findViewById(R.id.main)
        cicleTxt = findViewById(R.id.main_cicle)
        timerTxt = findViewById(R.id.main_timer)
        startTxt = findViewById(R.id.main_start)
        progressBar = findViewById(R.id.main_progressbar)

        startTxt.setOnClickListener {
            startTimer()
        }
    }

    private fun timer(){

        isRunning = true

        countDownTimer = object: CountDownTimer((time*60010).toLong(), 1000){
            override fun onTick(millisUntilFinished: Long) {

                timeFormat(millisUntilFinished)
                progressBar.max = time*60010
                progressBar.progress = millisUntilFinished.toInt()
            }

            override fun onFinish() {
                    shortBreak()
            }

        }.start()
    }

    private fun timeFormat(millisUntilFinished: Long)
    {
        val minutes = (millisUntilFinished/1000).toInt() / 60
        val seconds = (millisUntilFinished/1000).toInt() % 60
        val timeFormated = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        timerTxt.text = timeFormated
    }

    private fun startTimer(){

        if(isRunning == false){
            timer()
            startTxt.text = getString(R.string.pause)
        }else{
            timerPause()
            startTxt.text = getString(R.string.start)
        }
    }

    private fun timerPause(){
        countDownTimer.cancel()
        isRunning = false
    }

    private fun shortBreak(){
        mainLayout.setBackgroundColor(getColor(R.color.blue))
        //mainLayout.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.blue))
        cicleTxt.text = getString(R.string.cicle_1)
        startTxt.text = getString(R.string.start)
        isRunning = false
    }
}