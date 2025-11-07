package com.example.pomodoro

interface CycleController {

    fun goToTimerScreen(fragment: TimerFragment)

    fun goToShortBreakScreen(fragment: ShortBreakFragment)

    fun goToLongBreakScreen(fragment: LongBreakFragment)
}