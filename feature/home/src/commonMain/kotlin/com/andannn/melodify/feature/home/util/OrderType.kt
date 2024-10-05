package com.andannn.melodify.feature.home.util

sealed interface OrderType {
    object Ascending : OrderType
    object Descending : OrderType
}
