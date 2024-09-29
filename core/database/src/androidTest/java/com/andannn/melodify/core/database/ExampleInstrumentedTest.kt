package com.andannn.melodify.core.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andannn.melodify.core.database.entity.LyricEntity
import com.andannn.melodify.core.database.entity.LyricWithAudioCrossRef
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

    private val dummyCrossRef = listOf(
        LyricWithAudioCrossRef(
            mediaStoreId = 99,
            lyricId = 1
        ),
        LyricWithAudioCrossRef(
            mediaStoreId = 88,
            lyricId = 2
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
        lyricDao.insertLyricEntities(dummyLyricEntities)
        lyricDao.insertLyricWithAudioCrossRef(dummyCrossRef)

        val lyric = lyricDao.getLyricByMediaStoreId(99)
        assertEquals(dummyLyricEntities[0], lyric)
    }
}