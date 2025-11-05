package com.example.pomodoro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.pomodoro.databinding.DialogSettingsBinding

class SettingDialogFragment: DialogFragment(R.layout.dialog_settings) {

    private var binding: DialogSettingsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogSettingsBinding.bind(view)

        binding?.dialogButton?.setOnClickListener {

        }
    }

}