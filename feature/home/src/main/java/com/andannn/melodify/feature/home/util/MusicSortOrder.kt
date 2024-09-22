package com.andannn.melodify.feature.home.util

sealed class MusicSortOrder(
    open val orderType: OrderType
) {
    data class Name(override val orderType: OrderType) : MusicSortOrder(orderType)
    data class Date(override val orderType: OrderType) : MusicSortOrder(orderType)
    data class Size(override val orderType: OrderType) : MusicSortOrder(orderType)
    data class Length(override val orderType: OrderType) : MusicSortOrder(orderType)
}
