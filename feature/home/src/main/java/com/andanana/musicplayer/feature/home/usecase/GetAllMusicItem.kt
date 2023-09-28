package com.andanana.musicplayer.feature.home.usecase

import com.andanana.musicplayer.core.data.data.MediaStoreSource
import com.andanana.musicplayer.core.model.MusicInfo
import com.andanana.musicplayer.feature.home.util.MusicSortOrder
import com.andanana.musicplayer.feature.home.util.OrderType
import javax.inject.Inject

class GetAllMusicItem @Inject constructor(
    private val repository: MediaStoreSource
) {

    suspend operator fun invoke(
        musicSortOrder: MusicSortOrder = MusicSortOrder.Name(OrderType.Ascending)
    ): List<MusicInfo> {
//        return when (musicSortOrder) {
//            is MusicSortOrder.Date -> {
//                repository.getAllMusicInfo().sortedBy { it.modifiedDate }
//            }
//            is MusicSortOrder.Length -> {
//                repository.getAllMusicInfo().sortedBy { it.duration }
//            }
//            is MusicSortOrder.Name -> {
//                repository.getAllMusicInfo().sortedBy { it.title.lowercase() }
//            }
//            is MusicSortOrder.Size -> {
//                repository.getAllMusicInfo().sortedBy { it.size }
//            }
//        }.let {
//            if (musicSortOrder.orderType is OrderType.Descending) {
//                it.reversed()
//            } else {
//                it
//            }
//        }

        return emptyList()
    }
}
