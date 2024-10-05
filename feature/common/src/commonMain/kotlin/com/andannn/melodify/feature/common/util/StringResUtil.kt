package com.andannn.melodify.feature.common.util

import androidx.compose.runtime.Composable
import melodify.feature.common.generated.resources.Res
import melodify.feature.common.generated.resources.number_hours
import melodify.feature.common.generated.resources.number_minutes
import melodify.feature.common.generated.resources.number_seconds
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration

@Composable
fun durationString(duration: Duration): String {
    duration.toComponents { hours, minutes, seconds, nanoseconds ->
        val hasHours = hours != 0L
        val hasMinutes = minutes != 0
        val hasSeconds = seconds != 0 || nanoseconds != 0

        return buildString {
            if (hasHours) {
                append(stringResource(Res.string.number_hours, hours))
            }
            if (hasMinutes) {
                if (hasHours) {
                    append(" ")
                }
                append(stringResource(Res.string.number_minutes, minutes))
            }
            if (hasSeconds) {
                if (hasHours || hasMinutes) {
                    append(" ")
                }
                append(stringResource(Res.string.number_seconds, seconds))
            }
        }
    }
}
