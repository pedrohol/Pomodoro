package com.example.pomodoro.util

import com.example.pomodoro.view.LongBreakFragment
import com.example.pomodoro.view.ShortBreakFragment
import com.example.pomodoro.view.TimerFragment

interface CycleController {

    fun goToTimerScreen(fragment: TimerFragment)

    fun goToShortBreakScreen(fragment: ShortBreakFragment)

    fun goToLongBreakScreen(fragment: LongBreakFragment)
}