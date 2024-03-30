package com.andanana.musicplayer.core.data.util

import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.guava.await

suspend fun MediaBrowser.getChildrenById(mediaId: String): List<MediaItem> {
    return this.getChildren(
        mediaId,
        0,
        Int.MAX_VALUE,
        null,
    ).await().value ?: emptyList()
}

fun MediaBrowser.playMediaListFromStart(mediaList: List<MediaItem>) {
    playMediaList(mediaList, 0)
}

fun MediaBrowser.playMediaList(
    mediaList: List<MediaItem>,
    index: Int,
) {
    setMediaItems(
        mediaList,
        index,
        C.TIME_UNSET,
    )
    prepare()
    play()
}

fun ListenableFuture<MediaBrowser>.getOrNull() = if (isDone && !isCancelled) get() else null
