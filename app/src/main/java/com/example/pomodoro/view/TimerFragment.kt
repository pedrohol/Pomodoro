package com.example.pomodoro.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.util.CycleController
import com.example.pomodoro.R
import com.example.pomodoro.databinding.FragmentTimerBinding
import com.example.pomodoro.viewModel.CycleViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import android.media.Ringtone
import android.media.RingtoneManager

class TimerFragment: Fragment(R.layout.fragment_timer) {

    private var binding: FragmentTimerBinding? = null

    private var cycleController: CycleController? = null

    private val viewModel: CycleViewModel by viewModels()

    companion object {
        var cycle = 0
    }
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

            when {
                !isRunning && !resumeEnable -> {
                    binding?.skipButtom?.visibility = View.VISIBLE
                    binding?.timerStartTxt?.text = getString(R.string.pause)
                    isRunning = true
                    viewModel.startTimer()
                    Log.i("TESTE", "start")
                }

                isRunning && !resumeEnable -> {
                    binding?.timerStartTxt?.text = getString(R.string.resume)
                    binding?.skipButtom?.visibility = View.GONE
                    resumeEnable = true
                    isRunning = false
                    viewModel.timerPause()
                    Log.i("TESTE", "pause")
                }

                resumeEnable -> {
                    binding?.timerStartTxt?.text = getString(R.string.pause)
                    binding?.skipButtom?.visibility = View.VISIBLE
                    resumeEnable = false
                    isRunning = true
                    viewModel.timerResume()
                    Log.i("TESTE", "resume")
                }

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

        viewModel.viewModelScope.launch {
            viewModel.sharedFinishTimer.collect {
                cycle()
            }
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