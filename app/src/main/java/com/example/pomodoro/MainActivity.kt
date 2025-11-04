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
import com.example.pomodoro.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

   /* private lateinit var mainLayout: ConstraintLayout
    private lateinit var cicleTxt: TextView
    private lateinit var timerTxt: TextView
    private lateinit var startTxt: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var countDownTimer: CountDownTimer
    private var isRunning: Boolean = false
    private var time: Int = 1
    private var timeLeft: Int = 0

    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

       binding.mainSettingsBtn.setOnClickListener{
           val dialog = SettingDialogFragment()
           dialog.show(supportFragmentManager, dialog.tag)
       }

        /*mainLayout = findViewById(R.id.main)
        cicleTxt = findViewById(R.id.main_cicle)
        timerTxt = findViewById(R.id.main_timer)
        startTxt = findViewById(R.id.main_start)
        progressBar = findViewById(R.id.main_progressbar)

        startTxt.setOnClickListener {
            startTimer()
        }
         */
    }

    /*

    private fun timer(){

        isRunning = true

        countDownTimer = object: CountDownTimer((time*60000).toLong(), 1000){
            override fun onTick(millisUntilFinished: Long) {


                timeLeft = time - (millisUntilFinished/60000).toInt()
                progressBar.max = time*60000
                progressBar.progress = millisUntilFinished.toInt()
                timeFormat(millisUntilFinished)
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
    */
}