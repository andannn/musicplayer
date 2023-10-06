package com.andanana.musicplayer.core.player.util


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CoroutineTicker(
    private val delayMs: Long = 1000 / 30L,
    val action: () -> Unit
) : CoroutineScope {
    private var jobTracker: Job? = null

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main

    fun startTicker() {
        jobTracker = launch {
            while (true) {
                action()
                delay(delayMs)
            }
        }
    }

    fun stopTicker() {
        jobTracker?.cancel()
        jobTracker = null
    }
}
