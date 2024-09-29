package com.andannn.melodify.core.domain.model.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

private const val TAG = "CoroutineTicker"

class CoroutineTicker(
    private val delayMs: Long = 1000 / 30L,
    val action: () -> Unit,
) : CoroutineScope {
    private var jobTracker: Job? = null

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main

    fun startTicker() {
        if (jobTracker != null) {
            Timber.tag(TAG).d("startTicker: already started ${this.hashCode()}")
            return
        }

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
