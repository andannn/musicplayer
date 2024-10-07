package com.andannn.melodify

import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
// TODO: Check debug build
        Napier.base(DebugAntilog())

        startKoin {
            modules(modules)
        }
    }
) {
    MelodifyApp()
}