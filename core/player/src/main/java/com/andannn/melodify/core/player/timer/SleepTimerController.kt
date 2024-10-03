package com.andannn.melodify.core.player.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

sealed interface SleepTimeCounterState {
    data object Idle : SleepTimeCounterState

    data class Counting(val remain: Duration) : SleepTimeCounterState

    data object Finish : SleepTimeCounterState
}

interface SleepTimeCounterProvider {
    val counterState: SleepTimeCounterState

    fun getCounterStateFlow(): Flow<SleepTimeCounterState>
}

private const val TAG = "SleepTimerController"

interface SleepTimerController : SleepTimeCounterProvider {
    fun startTimer(duration: Duration)

    fun cancelTimer()
}

@Singleton
class SleepTimerControllerImpl
@Inject
constructor() : SleepTimerController, CoroutineScope {
    private val _counterState = MutableStateFlow<SleepTimeCounterState>(SleepTimeCounterState.Idle)

    private var _countingJob: Job? = null

    override val counterState: SleepTimeCounterState
        get() = _counterState.value

    override fun getCounterStateFlow() = _counterState

    override fun startTimer(duration: Duration) {
        _countingJob = launch {
            var currentRemainTime = duration

            while (true) {
                _counterState.value = SleepTimeCounterState.Counting(currentRemainTime)

                Timber.tag(TAG).d("_counterState ${_counterState.value}")

                delay(1000)
                currentRemainTime = currentRemainTime.minus(1.seconds)

                if (currentRemainTime.isNegative()) {
                    _counterState.value = SleepTimeCounterState.Finish
                    Timber.tag(TAG).d("_counterState ${_counterState.value}")
                    break
                }
            }
        }
    }

    override fun cancelTimer() {
        _countingJob?.cancel()
        _counterState.value = SleepTimeCounterState.Idle
        Timber.tag(TAG).d("_counterState ${_counterState.value}")
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default
}