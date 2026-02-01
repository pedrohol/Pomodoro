package com.example.pomodoro.view

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pomodoro.CycleController
import com.example.pomodoro.R
import com.example.pomodoro.databinding.FragmentTimerBinding
import com.example.pomodoro.viewModel.CycleViewModel
import java.util.Locale

class TimerFragment: Fragment(R.layout.fragment_timer) {

    private var binding: FragmentTimerBinding? = null

    private var cycleController: CycleController? = null

    private val viewModel: CycleViewModel by viewModels()

    companion object {
        var cycle = 0
    }

    private var timeProgress: Int = 0
    private val seconds: Int = 0
    private var isRunning: Boolean = false
    private var resumeEnable: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTimerBinding.bind(view)

        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.orange)

        setupListeners()
        setupObservers()

    }

    private fun setupListeners() {

        binding?.timerStartTxt?.setOnClickListener {

            if (!isRunning && !resumeEnable) {
                binding?.skipButtom?.visibility = View.VISIBLE
                binding?.timerStartTxt?.text = getString(R.string.pause)
                isRunning = true
                viewModel.startTimer()
            }

            if (isRunning && !resumeEnable) {
                binding?.timerStartTxt?.text = getString(R.string.resume)
                binding?.skipButtom?.visibility = View.GONE
                resumeEnable = true
                isRunning = false
                viewModel.timerPause()
            }

            if (resumeEnable) {
                binding?.timerStartTxt?.text = getString(R.string.pause)
                binding?.skipButtom?.visibility = View.VISIBLE
                resumeEnable = false
                isRunning = true
                viewModel.timerResume()
            }

        }

        binding?.skipButtom?.setOnClickListener {
            cycle()
        }

    }

    fun setupObservers() {
        viewModel.liveTimerProgress.observe(viewLifecycleOwner) { progress ->
            val minutes = (progress/1000).toInt() / 60
            val seconds = (progress/1000).toInt() % 60
            val timeFormated = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
            binding?.timerTimer?.text = timeFormated
        }

        viewModel.liveProgressBar.observe(viewLifecycleOwner) { progress ->
            binding?.timerProgressbar?.progress = progress
        }

        viewModel.setTimerTxt.observe(viewLifecycleOwner) { timer ->
            setTimeText(timer)
            val progress = timer * 60000
            binding?.timerProgressbar?.max = progress
            binding?.timerProgressbar?.progress = progress
        }

        viewModel.setTimerTxt.observe(viewLifecycleOwner) { timer ->
            setTimeText(timer)
        }

    }

    fun cycle() {
        cycle += 1

        if (cycle < 4){
            cycleController?.goToShortBreakScreen(ShortBreakFragment())
        } else {
            cycle = 0
            cycleController?.goToLongBreakScreen(LongBreakFragment())
        }
    }

    private fun setTimeText (time: Int){
        val timeFormat = String.format(Locale.getDefault(), "%02d:%02d", time, seconds)
        binding?.timerTimer?.text = timeFormat
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