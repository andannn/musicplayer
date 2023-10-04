package com.andanana.musicplayer.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.andanana.musicplayer.core.database.Tables
import com.andanana.musicplayer.core.database.entity.AlbumColumns
import com.andanana.musicplayer.core.database.entity.AlbumEntity
import com.andanana.musicplayer.core.database.entity.ArtistColumns
import com.andanana.musicplayer.core.database.entity.ArtistEntity
import com.andanana.musicplayer.core.database.entity.MusicColumns
import com.andanana.musicplayer.core.database.entity.MusicEntity
import com.andanana.musicplayer.core.database.entity.PlayListColumns
import com.andanana.musicplayer.core.database.entity.PlayListWithMusics
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {
    @Query("Delete FROM ${Tables.music}")
    suspend fun clearMusicEntity()

    @Query("Delete FROM ${Tables.artist}")
    suspend fun clearArtistEntity()

    @Query("Delete FROM ${Tables.album}")
    suspend fun clearAlbumEntity()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreMusicEntities(entities: List<MusicEntity>)

    @Query("SELECT * FROM ${Tables.music} WHERE ${MusicColumns.id} = :id")
    suspend fun getMusicById(id: Long): MusicEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreAlbumEntities(entities: List<AlbumEntity>)

    @Query("SELECT * FROM ${Tables.album} WHERE ${AlbumColumns.id} = :id")
    suspend fun getAlbumById(id: Long): AlbumEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreArtistEntities(entities: List<ArtistEntity>)

    @Query("SELECT * FROM ${Tables.artist} WHERE ${ArtistColumns.id} = :id")
    suspend fun getArtistById(id: Long): ArtistEntity

    @Query("SELECT * FROM ${Tables.music} WHERE ${MusicColumns.albumId} = :albumId")
    suspend fun getMusicsInAlbum(albumId: Long): List<MusicEntity>

    @Query("SELECT * FROM ${Tables.music} WHERE ${MusicColumns.albumId} = :albumId")
    fun getMusicsInAlbumFlow(albumId: Long): Flow<List<MusicEntity>>

    @Query("SELECT * FROM ${Tables.music} WHERE ${MusicColumns.artistId} = :artistId")
    suspend fun getMusicsInArtist(artistId: Long): List<MusicEntity>

    @Query("SELECT * FROM ${Tables.music} WHERE ${MusicColumns.artistId} = :artistId")
    fun getMusicsInArtistFlow(artistId: Long): Flow<List<MusicEntity>>

    @Query("SELECT * FROM ${Tables.artist}")
    suspend fun getAllArtists(): List<ArtistEntity>

    @Query("SELECT * FROM ${Tables.music}")
    suspend fun getAllMusics(): List<MusicEntity>

    @Query("SELECT * FROM ${Tables.album}")
    suspend fun getAllAlbums(): List<AlbumEntity>

    @Query("SELECT * FROM ${Tables.artist}")
    fun getAllArtistsFlow(): Flow<List<ArtistEntity>>

    @Query("SELECT * FROM ${Tables.album}")
    fun getAllAlbumsFlow(): Flow<List<AlbumEntity>>

    @Query("SELECT * FROM ${Tables.music}")
    fun getAllMusicsFlow(): Flow<List<MusicEntity>>

    @Transaction
    @Query("SELECT * FROM ${Tables.playList} WHERE ${PlayListColumns.id} = :playListId")
    fun getPlayListWithMusicsFlow(playListId: Long): Flow<PlayListWithMusics?>

    @Transaction
    @Query("SELECT * FROM ${Tables.playList} WHERE ${PlayListColumns.id} = :playListId")
    suspend fun getPlayListWithMusics(playListId: Long): PlayListWithMusics?
}
