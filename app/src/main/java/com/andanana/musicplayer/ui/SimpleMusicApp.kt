package com.andanana.musicplayer.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.andanana.musicplayer.core.designsystem.component.SmpNavigationBarItem
import com.andanana.musicplayer.core.designsystem.icons.Icon
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme
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
            val title = titleRes?.let { stringResource(id = it) } ?: ""
            val visible = !appState.isTopBarHide
            SmpCenterAlignedTopAppBar(
                visible = visible,
                title = title
            )
        },
        bottomBar = {
            val visible = !appState.isNavigationBarHide
            SimpleMusicNavigationBar(
                visible = visible,
                destinations = appState.topLevelDestinations,
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                currentDestination = appState.currentNavDestination
            )
        }
    ) {
        appState.systemUiController.setSystemBarsColor(color = MaterialTheme.colorScheme.surface)

        Column(modifier = Modifier.padding(it)) {
            SmpNavHost(
                modifier = Modifier.weight(1f),
                navHostController = appState.navController,
                onBackPressed = appState::onBackPressed
            )

            val backStackEntry by appState.navController.currentBackStackEntryAsState()
            val parentBackEntry = remember(backStackEntry) {
                appState.navController.getBackStackEntry(homeRoute)
            }
            MiniPlayerBox(playerStateViewModel = hiltViewModel(parentBackEntry))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SmpCenterAlignedTopAppBar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    title: String
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                Text(
                    text = title
                )
            }
        )
    }
}

@Composable
fun SimpleMusicNavigationBar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(),
        exit = shrinkVertically()
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
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

@Preview
@Composable
private fun SimpleMusicNavigationBarPreview() {
    MusicPlayerTheme {
        Surface {
            SimpleMusicNavigationBar(
                visible = true,
                destinations = TopLevelDestination.values().toList(),
                onNavigateToDestination = {},
                currentDestination = null
            )
        }
    }
}

@Preview
@Composable
private fun SmpCenterAlignedTopAppBarPreview() {
    MusicPlayerTheme {
        Surface {
            SmpCenterAlignedTopAppBar(
                visible = true,
                title = "Title"
            )
        }
    }
}
