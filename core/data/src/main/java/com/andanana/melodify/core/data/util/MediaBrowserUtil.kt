package com.andanana.melodify.core.data.util

import androidx.media3.session.MediaBrowser
import com.google.common.util.concurrent.ListenableFuture

fun ListenableFuture<MediaBrowser>.getOrNull() = if (isDone && !isCancelled) get() else null
