package com.andanana.musicplayer.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.andanana.musicplayer.core.designsystem.component.SmpNavigationBarItem
import com.andanana.musicplayer.core.designsystem.icons.Icon
import com.andanana.musicplayer.feature.home.navigation.homeRoute
import com.andanana.musicplayer.feature.player.MiniPlayerBox
import com.andanana.musicplayer.navigation.SmpNavHost
import com.andanana.musicplayer.navigation.TopLevelDestination

private const val TAG = "SimpleMusicApp"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleMusicApp(
    appState: SimpleMusicAppState = rememberSimpleMusicAppState()
) {
    Scaffold(
        topBar = {
            val titleRes = appState.currentTopLevelDestination?.titleTextId
            if (titleRes != null) {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(id = titleRes), color = Color.Black) }
                )
            }
        },
        bottomBar = {
            SimpleMusicNavigationBar(
                destinations = appState.topLevelDestinations,
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                currentDestination = appState.currentNavDestination
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            val onGetRootViewModelStoreOwner by rememberUpdatedState<() -> ViewModelStoreOwner> {
                appState.navController.getBackStackEntry(homeRoute)
            }

            SmpNavHost(
                modifier = Modifier.weight(1f),
                navHostController = appState.navController,
                onGetRootViewModelStoreOwner = onGetRootViewModelStoreOwner
            )
            MiniPlayerBox(rootViewModelStoreOwner = onGetRootViewModelStoreOwner.invoke())
        }
    }
}

@Composable
fun SimpleMusicNavigationBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            Log.d(TAG, "SimpleMusicNavigationBar: $destination $selected")
            SmpNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unSelectedIcon
                    }
                    when (icon) {
                        is Icon.ImageVectorIcon -> {
                            Icon(imageVector = icon.imageVector, contentDescription = "")
                        }
                    }
                },
                label = {
                    Text(stringResource(id = destination.iconTextId))
                }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
