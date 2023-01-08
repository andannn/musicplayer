package com.andanana.musicplayer.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.rememberNavController
import com.andanana.musicplayer.core.designsystem.icons.Icon
import com.andanana.musicplayer.navigation.SimpleMusicNavHost
import com.andanana.musicplayer.navigation.TopLevelDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleMusicApp(
    appState: SimpleMusicAppState = rememberSimpleMusicAppState()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "aaa", color = Color.Black) }
            )
        },
        bottomBar = {
            SimpleMusicNavigationBar(
                destinations = appState.topLevelDestinations,
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                currentDestination = appState.currentNavDestination
            )
        }
    ) {
        SimpleMusicNavHost(
            navHostController = rememberNavController()
        )
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
            NavigationBarItem(
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
