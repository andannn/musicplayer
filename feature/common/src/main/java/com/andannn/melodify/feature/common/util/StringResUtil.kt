package com.andannn.melodify.feature.common.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.andannn.melodify.common.R
import kotlin.time.Duration

@Composable
fun durationString(duration: Duration): String {
    duration.toComponents { hours, minutes, seconds, nanoseconds ->
        val hasHours = hours != 0L
        val hasMinutes = minutes != 0
        val hasSeconds = seconds != 0 || nanoseconds != 0

        return buildString {
            if (hasHours) {
                append(stringResource(id = R.string.number_hours, hours))
            }
            if (hasMinutes) {
                if (hasHours) {
                    append(" ")
                }
                append(stringResource(id = R.string.number_minutes, minutes))
            }
            if (hasSeconds) {
                if (hasHours || hasMinutes) {
                    append(" ")
                }
                append(stringResource(id = R.string.number_seconds, seconds))
            }
        }
    }
}
