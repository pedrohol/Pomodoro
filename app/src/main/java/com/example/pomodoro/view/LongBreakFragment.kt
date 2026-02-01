package com.example.pomodoro.view

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.util.CycleController
import com.example.pomodoro.R
import com.example.pomodoro.databinding.FragmentLongBreakBinding
import com.example.pomodoro.viewModel.CycleViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class LongBreakFragment: Fragment(R.layout.fragment_long_break) {

    private var binding: FragmentLongBreakBinding? = null

    private var cycleController: CycleController? = null

    private val viewModel: CycleViewModel by viewModels()

    private val seconds: Int = 0
    private var isRunning: Boolean = false
    private var resumeEnable: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLongBreakBinding.bind(view)

        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.dark_blue)

        setupListeners()
        setupObservers()

    }


    private fun setupListeners() {
        binding?.longBreakStartTxt?.setOnClickListener {

            when {
                !isRunning && !resumeEnable -> {
                    binding?.longBreakSkipButtom?.visibility = View.VISIBLE
                    binding?.longBreakStartTxt?.text = getString(R.string.pause)
                    isRunning = true
                    viewModel.startLong()
                }

                isRunning && !resumeEnable -> {
                    binding?.longBreakStartTxt?.text = getString(R.string.resume)
                    binding?.longBreakSkipButtom?.visibility = View.GONE
                    resumeEnable = true
                    isRunning = false
                    viewModel.timerPause()
                }

                resumeEnable -> {
                    binding?.longBreakStartTxt?.text = getString(R.string.pause)
                    binding?.longBreakSkipButtom?.visibility = View.VISIBLE
                    resumeEnable = false
                    isRunning = true
                    viewModel.timerResume()
                }
            }
        }

        binding?.longBreakSkipButtom?.setOnClickListener {
            onFinish()
        }
    }

    private fun setupObservers() {
        viewModel.liveTimerProgress.observe(viewLifecycleOwner) { progress ->
            val minutes = (progress/1000).toInt() / 60
            val seconds = (progress/1000).toInt() % 60
            val timeFormated = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
            binding?.longBreakTimerTxt?.text = timeFormated
        }

        viewModel.liveProgressBar.observe(viewLifecycleOwner) { progress ->
            binding?.longBreakProgressbar?.progress = progress
        }

        viewModel.setTimerTxt.observe(viewLifecycleOwner) { timer ->
            setTimeText(timer)
            val progress = timer * 60000
            binding?.longBreakProgressbar?.max = progress
            binding?.longBreakProgressbar?.progress = progress
        }

        viewModel.setTimerTxt.observe(viewLifecycleOwner) { timer ->
            setTimeText(timer)
        }

        viewModel.viewModelScope.launch {
            viewModel.sharedFinishTimer.collect {
                onFinish()
            }
        }
    }

    private fun setTimeText(time: Int) {
        val timeFormat = String.format(Locale.getDefault(), "%02d:%02d", time, seconds)
        binding?.longBreakTimerTxt?.text = timeFormat
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