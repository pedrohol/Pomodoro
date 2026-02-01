package com.example.pomodoro.view

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.util.CycleController
import com.example.pomodoro.R
import com.example.pomodoro.databinding.FragmentShortBreakBinding
import com.example.pomodoro.viewModel.CycleViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class ShortBreakFragment: Fragment(R.layout.fragment_short_break) {

    private var binding: FragmentShortBreakBinding? = null

    private var cycleController: CycleController? = null

    private val viewModel: CycleViewModel by viewModels()

    private val seconds: Int = 0
    private var isRunning: Boolean = false
    private var resumeEnable: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentShortBreakBinding.bind(view)

        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.blue)

        setupListeners()
        setupObeservers()

    }

    private fun setupListeners() {

        binding?.shortBreakStartTxt?.setOnClickListener {

            when {
                !isRunning && !resumeEnable -> {
                    binding?.shortBreakSkipButtom?.visibility = View.VISIBLE
                    binding?.shortBreakStartTxt?.text = getString(R.string.pause)
                    isRunning = true
                    viewModel.startShort()
                }

                isRunning && !resumeEnable -> {
                    binding?.shortBreakStartTxt?.text = getString(R.string.resume)
                    binding?.shortBreakSkipButtom?.visibility = View.GONE
                    resumeEnable = true
                    isRunning = false
                    viewModel.timerPause()
                }

                resumeEnable -> {
                    binding?.shortBreakStartTxt?.text = getString(R.string.pause)
                    binding?.shortBreakSkipButtom?.visibility = View.VISIBLE
                    resumeEnable = false
                    isRunning = true
                    viewModel.timerResume()
                }
            }
        }

        binding?.shortBreakSkipButtom?.setOnClickListener {
            onFinish()
        }

    }

    fun setupObeservers() {
        viewModel.liveTimerProgress.observe(viewLifecycleOwner) { progress ->
            val minutes = (progress/1000).toInt() / 60
            val seconds = (progress/1000).toInt() % 60
            val timeFormated = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
            binding?.shortBreakTimerTxt?.text = timeFormated
        }

        viewModel.liveProgressBar.observe(viewLifecycleOwner) { progress ->
            binding?.shortBreakProgressbar?.progress = progress
        }

        viewModel.setShortTxt.observe(viewLifecycleOwner) { timer ->
            setTimeText(timer)
            val progress = timer * 60000
            binding?.shortBreakProgressbar?.max = progress
            binding?.shortBreakProgressbar?.progress = progress
        }

        viewModel.setShortTxt.observe(viewLifecycleOwner) { timer ->
            setTimeText(timer)
        }

        viewModel.viewModelScope.launch {
            viewModel.sharedFinishTimer.collect {
                onFinish()
            }
        }

    }

    private fun setTimeText (time: Int) {
        val timeFormat = String.format(Locale.getDefault(), "%02d:%02d", time, seconds)
        binding?.shortBreakTimerTxt?.text = timeFormat
    }

    private fun onFinish() {
        cycleController?.goToTimerScreen(TimerFragment())
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