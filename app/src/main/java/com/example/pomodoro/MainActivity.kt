package com.example.pomodoro

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.databinding.ActivityMainBinding
import com.example.pomodoro.util.CycleController
import com.example.pomodoro.view.LongBreakFragment
import com.example.pomodoro.view.SettingDialogFragment
import com.example.pomodoro.view.ShortBreakFragment
import com.example.pomodoro.view.TimerFragment
import com.example.pomodoro.viewModel.CycleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CycleController {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(2000)
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.mainSettingsBtn.setOnClickListener {
            val dialog = SettingDialogFragment()
            dialog.show(supportFragmentManager, dialog.tag)
        }

    }

    override fun goToTimerScreen(fragment: TimerFragment) {
        playNotificationSound()
        vibratePhone()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun goToShortBreakScreen(fragment: ShortBreakFragment) {
        playNotificationSound()
        vibratePhone()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun goToLongBreakScreen(fragment: LongBreakFragment) {
        playNotificationSound()
        vibratePhone()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun playNotificationSound() {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mediaPlayer = MediaPlayer.create(this, notification)
        mediaPlayer?.apply {
            start()
            setOnCompletionListener { mp ->
                mp.release()
            }
        }
    }

    private fun vibratePhone() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(500) //
        }
    }
}