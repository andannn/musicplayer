package com.andanana.musicplayer.core.player

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import com.andanana.musicplayer.core.player.repository.PlayerController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : Service() {

    @Inject
    lateinit var playerController: PlayerController

    @Inject
    lateinit var smpNotificationManager: SmpNotificationManager

    private lateinit var mediaSession: MediaSessionCompat

    override fun onBind(intent: Intent?): IBinder? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        playerController.initialize()

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
        playerController.release()
    }

    companion object {
        const val MEDIA_SESSION = "media_session"
        const val PLAYER_SERVICE = "smp_player"
    }
}
