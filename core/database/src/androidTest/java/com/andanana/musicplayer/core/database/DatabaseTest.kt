package com.andanana.musicplayer.core.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andanana.musicplayer.core.database.dao.MusicDao
import com.andanana.musicplayer.core.database.dao.PlayListDao
import com.andanana.musicplayer.core.database.entity.AlbumEntity
import com.andanana.musicplayer.core.database.entity.ArtistEntity
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
        MusicEntity(id = 1, title = "musicA", albumId = 11, album = "AlbumA", artistId = 31),
        MusicEntity(id = 2, title = "musicB", albumId = 11, album = "AlbumA", artistId = 41),
        MusicEntity(id = 3, title = "musicC", albumId = 22, album = "AlbumB", artistId = 41),
    )

    private val dummyAlbumEntities = listOf(
        AlbumEntity(albumId = 11, title = "album A"),
        AlbumEntity(albumId = 22, title = "album B"),
    )

    private val dummyArtistEntities = listOf(
        ArtistEntity(artistId = 31, name = "artist A"),
        ArtistEntity(artistId = 41, name = "artist B"),
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
    fun get_music_by_id() = runTest {
        musicDao.insertOrIgnoreMusicEntities(dummyMusicEntities)
        val result = musicDao.getMusicById(1)

        assertEquals(result, dummyMusicEntities[0])
    }

    @Test
    fun get_musics_in_album() = runTest {
        musicDao.insertOrIgnoreMusicEntities(dummyMusicEntities)
        musicDao.insertOrIgnoreAlbumEntities(dummyAlbumEntities)

        val result = musicDao.getMusicsInAlbum(11)
        assertEquals(result, dummyMusicEntities.subList(fromIndex = 0, toIndex = 2))
    }

    @Test
    fun get_musics_in_artist() = runTest {
        musicDao.insertOrIgnoreMusicEntities(dummyMusicEntities)
        musicDao.insertOrIgnoreAlbumEntities(dummyAlbumEntities)
        musicDao.insertOrIgnoreArtistEntities(dummyArtistEntities)

        val result = musicDao.getMusicsInArtist(41)
        assertEquals(result, dummyMusicEntities.subList(fromIndex = 1, toIndex = 3))
    }


//
//    @Test
//    fun upsert_artist_entities() = runTest {
//        musicDao.insertOrIgnoreMusicEntities(dummyMusicEntities.toList())
//    }
//
//    @Test
//    fun upsert_album_entities() = runTest {
//        musicDao.insertOrIgnoreMusicEntities(dummyMusicEntities.toList())
//    }
}