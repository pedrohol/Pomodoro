package com.example.pomodoro

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pomodoro.databinding.FragmentTimerBinding
import java.util.Locale

class TimerFragment: Fragment(R.layout.fragment_timer) {

    private var binding: FragmentTimerBinding? = null

    private lateinit var countDownTimer: CountDownTimer

    private var time: Int = 1
    private val seconds: Int = 0
    private var timeLeft: Long = 0
    private var timeProgress: Int = 0
    private var isRunning: Boolean = false
    private var resumeEnable: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTimerBinding.bind(view)

        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.orange)

        val timeFormat = String.format(Locale.getDefault(), "%02d:%02d", time, seconds)
        binding?.timerTimer?.text = timeFormat

        binding?.timerStartTxt?.setOnClickListener {

            if (!isRunning && !resumeEnable) {
                timerStart(time)
            } else if (isRunning && !resumeEnable) {
                timerPause()
            } else if (resumeEnable) {
                timerResume()
            }
        }

        binding?.skipButtom?.setOnClickListener {
            countDownTimer.onFinish()
        }

    }

    private fun timer (timeLenght: Long) {
        isRunning = true

        countDownTimer = object: CountDownTimer(timeLenght, 1000){
            override fun onTick(millisUntilFinished: Long) {

                timeLeft = millisUntilFinished
                timeProgress = millisUntilFinished.toInt()
                binding?.timerProgressbar?.progress = timeProgress
                timeFormat(millisUntilFinished)
            }

            override fun onFinish() {

            }

        }.start()
    }

    private fun timeFormat (millisUntilFinished: Long) {
        val minutes = (millisUntilFinished/1000).toInt() / 60
        val seconds = (millisUntilFinished/1000).toInt() % 60
        val timeFormated = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        binding?.timerTimer?.text = timeFormated
    }

    private fun timerStart(minutes: Int) {
        var time = (minutes * 60000).toLong()
        timer(time)

        binding?.timerProgressbar?.max = time.toInt()
        binding?.skipButtom?.visibility = View.VISIBLE
        binding?.timerStartTxt?.text = getString(R.string.pause)
    }

    private fun timerPause() {
        countDownTimer.cancel()
        binding?.timerStartTxt?.text = getString(R.string.resume)
        binding?.skipButtom?.visibility = View.GONE
        resumeEnable = true
        isRunning = false
    }

    private fun timerResume() {
        timer(timeLeft)
        binding?.timerStartTxt?.text = getString(R.string.pause)
        binding?.skipButtom?.visibility = View.VISIBLE
        resumeEnable = false
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}