package com.andanana.musicplayer.feature.home.util

sealed interface OrderType {
    object Ascending : OrderType
    object Descending : OrderType
}
