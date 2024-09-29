package com.andannn.melodify.core.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andannn.melodify.core.database.entity.LyricEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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
    private lateinit var dataBase: MelodifyDataBase
    private lateinit var lyricDao: LyricDao

    private val dummyLyricEntities = listOf(
        LyricEntity(
            id = 1,
            name = "name",
            trackName = "trackName",
            artistName = "artistName",
            albumName = "albumName",
            duration = 1.0,
            instrumental = true,
            plainLyrics = "plainLyrics",
            syncedLyrics = "syncedLyrics"
        ),
        LyricEntity(
            id = 2,
            name = "name",
            trackName = "trackName",
            artistName = "artistName",
            albumName = "albumName",
            duration = 1.0,
            instrumental = true,
            plainLyrics = "plainLyrics",
            syncedLyrics = "syncedLyrics"
        )
    )

    @Before
    fun setUpDatabase() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MelodifyDataBase::class.java
        ).allowMainThreadQueries().build()

        lyricDao = dataBase.getLyricDao()
    }

    @After
    fun closeDatabase() {
        dataBase.close()
    }

    @Test
    fun get_lyric_by_media_store_id() = runBlocking {
        lyricDao.insertLyricOfMedia(mediaStoreId = 99, lyric = dummyLyricEntities[0])

        val lyric = lyricDao.getLyricByMediaStoreIdFlow(99).first()
        assertEquals(dummyLyricEntities[0], lyric)
    }

    @Test
    fun get_lyric_by_media_store_id_not_exist() = runBlocking {
        lyricDao.insertLyricOfMedia(mediaStoreId = 99, lyric = dummyLyricEntities[0])

        val lyric = lyricDao.getLyricByMediaStoreIdFlow(100).first()
        assertEquals(null, lyric)
    }
}