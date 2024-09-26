package com.andannn.melodify.core.player

import android.app.Application
import android.content.ComponentName
import android.util.Log
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.jvm.Throws

private const val TAG = "MediaBrowserManager"

interface MediaBrowserManager {
    val mediaBrowser: MediaBrowser

    @Throws(TimeoutCancellationException::class)
    suspend fun connect()

    fun disConnect()
}

@Singleton
class MediaBrowserManagerImpl
@Inject
constructor(
    private val application: Application,
) : MediaBrowserManager {
    private var _mediaBrowser: MediaBrowser? = null

    override val mediaBrowser: MediaBrowser
        get() {
            return _mediaBrowser ?: throw IllegalStateException("MediaBrowser is not initialized")
        }

    override suspend fun connect() {
        Log.d(TAG, "connect: start")
        _mediaBrowser = withTimeout(5000) {
            providerMediaBrowser(application).await()
        }
        Log.d(TAG, "connect: finish")
    }

    override fun disConnect() {
        _mediaBrowser?.release()
        _mediaBrowser = null
    }
}

private fun providerMediaBrowser(application: Application): ListenableFuture<MediaBrowser> {
    return MediaBrowser.Builder(
        application,
        SessionToken(
            application,
            ComponentName(application, PlayerService::class.java.name),
        ),
    )
        .buildAsync()
}