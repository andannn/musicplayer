package com.andanana.musicplayer.feature.home.navigation

import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.andanana.musicplayer.feature.home.HomeRoute

const val homeRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onGetRootViewModelStoreOwner: () -> ViewModelStoreOwner
) {
    composable(route = homeRoute) {
        HomeRoute(onGetRootViewModelStoreOwner = onGetRootViewModelStoreOwner)
    }
}
