package com.andannn.melodify.feature.common.drawer

import com.andannn.melodify.core.data.model.AlbumItemModel
import com.andannn.melodify.core.data.model.ArtistItemModel
import com.andannn.melodify.core.data.model.AudioItemModel
import com.andannn.melodify.core.data.model.GenreItemModel
import com.andannn.melodify.core.data.model.MediaItemModel
import com.andannn.melodify.feature.common.icons.SimpleMusicIcons
import com.andannn.melodify.feature.common.icons.SmpIcon
import melodify.feature.common.generated.resources.Res
import melodify.feature.common.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Duration

sealed interface SheetModel {
    abstract class MediaOptionSheet(
        open val source: MediaItemModel,
        open val options: List<SheetOptionItem>,
    ) : SheetModel {
        companion object {
            fun fromMediaModel(item: MediaItemModel): SheetModel {
                return when (item) {
                    is AlbumItemModel -> AlbumOptionSheet(item)
                    is ArtistItemModel -> ArtistOptionSheet(item)
                    is AudioItemModel -> AudioOptionSheet(item)
                    is GenreItemModel -> GenreOptionSheet(item)
                }
            }
        }
    }

    data class AudioOptionSheet(override val source: AudioItemModel) : MediaOptionSheet(
        source = source,
        options = listOf(
            SheetOptionItem.ADD_TO_QUEUE,
            SheetOptionItem.PLAY_NEXT,
            SheetOptionItem.DELETE,
        ),
    )

    data class GenreOptionSheet(override val source: GenreItemModel) : MediaOptionSheet(
        source = source,
        options = listOf(
            SheetOptionItem.ADD_TO_QUEUE,
            SheetOptionItem.PLAY_NEXT,
            SheetOptionItem.DELETE,
        )
    )

    data class PlayerOptionSheet(override val source: AudioItemModel) : MediaOptionSheet(
        source = source,
        options = listOf(
            SheetOptionItem.ADD_TO_QUEUE,
            SheetOptionItem.PLAY_NEXT,
            SheetOptionItem.SLEEP_TIMER,
        ),
    )

    data class AlbumOptionSheet(override val source: AlbumItemModel) : MediaOptionSheet(
        source = source,
        options = listOf(
            SheetOptionItem.ADD_TO_QUEUE,
            SheetOptionItem.PLAY_NEXT,
            SheetOptionItem.DELETE,
        ),
    )

    data class ArtistOptionSheet(override val source: ArtistItemModel) : MediaOptionSheet(
        source = source,
        options = listOf(
            SheetOptionItem.ADD_TO_QUEUE,
            SheetOptionItem.PLAY_NEXT,
            SheetOptionItem.DELETE,
        ),
    )

    data object TimerOptionSheet : SheetModel

    data class TimerRemainTimeSheet(
        val remainTime: Duration
    ) : SheetModel
}

enum class SheetOptionItem(
    val smpIcon: SmpIcon,
    val text: StringResource,
) {
    PLAY_NEXT(
        smpIcon = SimpleMusicIcons.PlayNext,
        text = Res.string.play_next,
    ),
    DELETE(
        smpIcon = SimpleMusicIcons.Delete,
        text = Res.string.delete,
    ),
    ADD_TO_QUEUE(
        smpIcon = SimpleMusicIcons.Delete,
        text = Res.string.add_to_queue,
    ),
    SLEEP_TIMER(
        smpIcon = SimpleMusicIcons.Timer,
        text = Res.string.sleep_timer,
    ),
}
