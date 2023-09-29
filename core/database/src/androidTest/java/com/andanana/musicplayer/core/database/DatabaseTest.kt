package com.andanana.musicplayer.core.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andanana.musicplayer.core.database.dao.MusicDao
import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.MusicEntity
import kotlinx.coroutines.test.runTest
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var dataBase: SmpDataBase
    private lateinit var musicDao: MusicDao
    private lateinit var playListDao: PlayListDao

    private val dummyMusicEntities = listOf(
        MusicEntity(id = 1, title = "musicA", albumId = 11, album = "AlbumA"),
        MusicEntity(id = 2, title = "musicB", albumId = 11, album = "AlbumA"),
        MusicEntity(id = 3, title = "musicC", albumId = 22, album = "AlbumB"),
    )

    @Before
    fun setUpDatabase() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SmpDataBase::class.java
        ).allowMainThreadQueries().build()

        musicDao = dataBase.musicDao()
        playListDao = dataBase.playListDao()
    }

    @After
    fun closeDatabase() {
        dataBase.close()
    }

    @Test
    fun upsert_music_entities() = runTest {
        musicDao.insertOrIgnoreMusicEntities(dummyMusicEntities.toList())
    }
}