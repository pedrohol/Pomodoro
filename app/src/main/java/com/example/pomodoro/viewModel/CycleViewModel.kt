package com.example.pomodoro.viewModel

import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.R
import com.example.pomodoro.view.LongBreakFragment
import com.example.pomodoro.view.ShortBreakFragment
import com.example.pomodoro.view.TimerFragment.Companion.cycle
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Locale

class CycleViewModel: ViewModel() {

    private var countDownTimer: CountDownTimer? = null
    private var timeProgress: Int = 0
    private var timeLeft: Long = 0

    val setTimerTxt: LiveData<Int> = setTimer

    private val timerProgress = MutableLiveData<Long>()
    val liveTimerProgress: LiveData<Long> = timerProgress

    private val progressBar = MutableLiveData<Int>()
    val liveProgressBar: LiveData<Int> = progressBar

    private val finishTimer = MutableSharedFlow<Unit>()
    var sharedFinishTimer = finishTimer.asSharedFlow()

    private var isPaused: Boolean = false


    companion object {
        val setTimer = MutableLiveData<Int>()
        val setShort = MutableLiveData<Int>()
        val setLong = MutableLiveData<Int>()

    }

    init {
        if(setTimer.value == null) {
            setTimer.value = 30
            setShort.value = 5
            setLong.value = 15
        }
    }

    private fun timer (timeLenght: Long) {

        countDownTimer?.cancel()
        countDownTimer = null


        countDownTimer = object: CountDownTimer(timeLenght, 1000){
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                timeProgress = millisUntilFinished.toInt()
                timerProgress.value = millisUntilFinished
                progressBar.value = timeProgress
            }

            override fun onFinish() {
                timeLeft = 0
                isPaused = false
                viewModelScope.launch {
                    finishTimer.emit(Unit)
                }
            }

        }.start()

        isPaused = false

    }

    fun setTimer(time: Int) {
        setTimer.value = time
    }


    fun setShort(time: Int) {
        setShort.value = time
    }

    fun setLong(time: Int) {
        setLong.value = time
    }

    fun startTimer() {
        val minutes = setTimer.value
        var time = (minutes * 60000).toLong()
        timeLeft = time
        timer(time)
    }

    fun startShort() {
        val minutes = setShort.value
        var time = (minutes * 60000).toLong()
        timeLeft = time
        timer(time)
    }

    fun startLong() {
        val minutes = setLong.value
        var time = (minutes * 60000).toLong()
        timeLeft = time
        timer(time)
    }

    fun timerPause() {
        if (!isPaused) {
            countDownTimer?.cancel()
            countDownTimer = null
            isPaused = true
        }

    }
    fun timerResume() {
        if (isPaused && timeLeft > 0) {
            timer(timeLeft)
        }
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
        countDownTimer = null
    }

}