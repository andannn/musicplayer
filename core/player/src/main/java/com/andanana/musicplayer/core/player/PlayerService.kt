package com.andanana.musicplayer.core.player

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.andanana.musicplayer.core.player.repository.PlayerRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : Service() {

    @Inject
    lateinit var playerRepository: PlayerRepository

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        playerRepository.initial()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        playerRepository.release()
    }
}
