package com.example.pomodoro

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pomodoro.databinding.FragmentTimerBinding
import java.util.Locale

class TimerFragment: Fragment(R.layout.fragment_timer) {

    private var binding: FragmentTimerBinding? = null

    private lateinit var countDownTimer: CountDownTimer

    private var time: Int = 1
    private var timeLeft: Long = 0
    private var timeProgress: Int = 0
    private var maxProgress: Int = 0
    private var isRunning: Boolean = false
    private var resumeEnable: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTimerBinding.bind(view)

        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.orange)

        binding?.timerStartTxt?.setOnClickListener {

            if (!isRunning && !resumeEnable) {
                timerStart(time)
            } else if (isRunning && !resumeEnable){
                timerPause()
            } else if (resumeEnable){
                timerResume()
            }
        }
    }

    private fun timer (timeLenght: Long) {
        isRunning = true

        countDownTimer = object: CountDownTimer(timeLenght, 1000){
            override fun onTick(millisUntilFinished: Long) {

                timeLeft = millisUntilFinished
                timeProgress = millisUntilFinished.toInt()
                maxProgress = timeLenght.toInt()
                binding?.timerProgressbar?.max = maxProgress
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
        binding?.timerStartTxt?.text = getString(R.string.pause)
    }

    private fun timerPause(){
        countDownTimer.cancel()
        binding?.timerStartTxt?.text = getString(R.string.resume)
        resumeEnable = true
        isRunning = false
    }

    private fun timerResume(){
        timer(timeLeft)
        binding?.timerStartTxt?.text = getString(R.string.pause)
        resumeEnable = false
    }
}