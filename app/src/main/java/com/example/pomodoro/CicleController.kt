package com.example.pomodoro

interface CicleController {

    fun goToTimerScreen(fragment: TimerFragment)

    fun goToShortBreakScreen(fragment: ShortBreakFragment)

    fun goToLongBreakScreen(fragment: LongBreakFragment)
}