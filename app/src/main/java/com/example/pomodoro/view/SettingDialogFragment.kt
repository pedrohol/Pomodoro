package com.example.pomodoro.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.pomodoro.R
import com.example.pomodoro.databinding.DialogSettingsBinding
import com.example.pomodoro.viewModel.CycleViewModel

class SettingDialogFragment: DialogFragment(R.layout.dialog_settings) {

    private var binding: DialogSettingsBinding? = null

    private val viewModel: CycleViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogSettingsBinding.bind(view)

        binding?.dialogButton?.setOnClickListener {

            val timer = binding?.dialogTimerEditText?.text.toString().toInt()
            viewModel.setTimer(timer)

            val short = binding?.dialogShortBreakEditText?.text.toString().toInt()
            viewModel.setShort(short)

            val long = binding?.dialogLongBreakEditText?.text.toString().toInt()
            viewModel.setLong(long)

            dismiss()
        }
    }
}