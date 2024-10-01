package com.andannn.melodify.core.designsystem.theme

import androidx.compose.ui.graphics.Color
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andannn.melodify.core.designsystem.theme.util.ColorSchemeUtil

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import kotlin.time.measureTime

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val time = measureTime {
            repeat(100) {
                ColorSchemeUtil.fromSeed(seedColor = Color.Red, isDark = true)
            }
        }

        println("Average time is ${time / 100.0}")
    }
}