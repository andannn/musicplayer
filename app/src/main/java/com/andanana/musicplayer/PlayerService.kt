package com.andanana.musicplayer

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import com.andanana.musicplayer.core.player.repository.PlayerRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : Service() {

    @Inject
    lateinit var playerRepository: PlayerRepository

    @Inject
    lateinit var smpNotificationManager: SmpNotificationManager

    private lateinit var mediaSession: MediaSessionCompat

    override fun onBind(intent: Intent?): IBinder? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        playerRepository.initialize()

        mediaSession = MediaSessionCompat(this, MEDIA_SESSION)

        startForeground(
            10,
            smpNotificationManager.getPlayerNotification(
                session = mediaSession
            )
        )

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        playerRepository.release()
    }

    companion object {
        const val MEDIA_SESSION = "media_session"
        const val PLAYER_SERVICE = "smp_player"
    }
}
