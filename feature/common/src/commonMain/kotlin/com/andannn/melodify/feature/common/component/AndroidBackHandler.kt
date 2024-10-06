package com.andannn.melodify.feature.common.component

import androidx.compose.runtime.Composable

@Composable
expect fun AndroidBackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit
)