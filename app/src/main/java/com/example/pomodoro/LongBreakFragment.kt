package com.example.pomodoro

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pomodoro.databinding.FragmentLongBreakBinding
import java.util.Locale

class LongBreakFragment: Fragment(R.layout.fragment_long_break) {

    private var binding: FragmentLongBreakBinding? = null

    private var cycleController: CycleController? = null

    private lateinit var countDownTimer: CountDownTimer

    private var time: Int = 15
    private val seconds: Int = 0
    private var timeLeft: Long = 0
    private var timeProgress: Int = 0
    private var isRunning: Boolean = false
    private var resumeEnable: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLongBreakBinding.bind(view)

        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.dark_blue)

        val timeFormat = String.format(Locale.getDefault(), "%02d:%02d", time, seconds)
        binding?.longBreakTimerTxt?.text = timeFormat

        binding?.longBreakStartTxt?.setOnClickListener {

            if (!isRunning && !resumeEnable) {
                timerStart(time)
            } else if (isRunning && !resumeEnable) {
                timerPause()
            } else if (resumeEnable) {
                timerResume()
            }
        }

        binding?.longBreakSkipButtom?.setOnClickListener {
            countDownTimer.onFinish()
        }
    }

    private fun timer (timeLenght: Long) {
        isRunning = true

        countDownTimer = object: CountDownTimer(timeLenght, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                timeProgress = millisUntilFinished.toInt()
                binding?.longBreakProgressbar?.progress = timeProgress
                timeFormat(millisUntilFinished)
            }

            override fun onFinish() {
                cycleController?.goToTimerScreen(TimerFragment())
            }
        }.start()
    }

    private fun timeFormat (millisUntilFinished: Long) {
        val minutes = (millisUntilFinished / 1000).toInt() / 60
        val seconds = (millisUntilFinished / 1000).toInt() % 60
        val timeFormated = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        binding?.longBreakTimerTxt?.text = timeFormated
    }

    private fun timerStart (minutes: Int) {
        var time = (minutes * 60000).toLong()
        timer(time)

        binding?.longBreakProgressbar?.max = time.toInt()
        binding?.longBreakSkipButtom?.visibility = View.VISIBLE
        binding?.longBreakStartTxt?.text = getString(R.string.pause)
    }

    private fun timerPause() {
        countDownTimer.cancel()
        binding?.longBreakStartTxt?.text = getString(R.string.resume)
        binding?.longBreakSkipButtom?.visibility = View.GONE
        resumeEnable = true
        isRunning = false
    }

    private fun timerResume() {
        timer(timeLeft)
        binding?.longBreakStartTxt?.text = getString(R.string.pause)
        binding?.longBreakSkipButtom?.visibility = View.VISIBLE
        resumeEnable = false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CycleController) {
            cycleController = context
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}