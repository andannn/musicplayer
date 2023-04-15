package com.andanana.musicplayer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.andanana.musicplayer.MainActivityViewModel
import com.andanana.musicplayer.core.designsystem.DrawerItem
import com.andanana.musicplayer.core.designsystem.component.DrawerItemView
import com.andanana.musicplayer.core.designsystem.component.SmpBottomDrawer
import com.andanana.musicplayer.core.designsystem.component.SmpNavigationBarItem
import com.andanana.musicplayer.core.designsystem.icons.Icon
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme
import com.andanana.musicplayer.feature.home.navigation.homeRoute
import com.andanana.musicplayer.feature.library.navigation.navigateToAddPlayListDialog
import com.andanana.musicplayer.feature.library.navigation.navigateToNewPlayListDialog
import com.andanana.musicplayer.feature.player.MiniPlayerBox
import com.andanana.musicplayer.feature.player.PlayerRoute
import com.andanana.musicplayer.navigation.SmpNavHost
import com.andanana.musicplayer.navigation.TopLevelDestination

private const val TAG = "SimpleMusicApp"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SimpleMusicApp(
    appState: SimpleMusicAppState = rememberSimpleMusicAppState()
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No view model store owner"
    }
    val mainViewModel: MainActivityViewModel = remember(viewModelStoreOwner) {
        ViewModelProvider(viewModelStoreOwner)[MainActivityViewModel::class.java]
    }

    Scaffold(
        snackbarHost = {
            val interactingItem by mainViewModel.interactingUri.collectAsState()

            val snackBarPaddingBottom =
                remember(
                    appState.currentNavDestination,
                    interactingItem
                ) {
                    getSnackBarPaddingBottom(
                        appState = appState,
                        isMusicBoxShowing = interactingItem != null
                    )
                }
            SnackbarHost(
                modifier = Modifier.padding(bottom = snackBarPaddingBottom),
                hostState = appState.snackbarHostState
            )
        },
        topBar = {
            val titleRes = appState.currentTopLevelDestination?.titleTextId
            val title = titleRes?.let { stringResource(id = it) } ?: ""

            val visible = remember(appState.currentNavDestination) {
                !appState.isTopBarHide
            }
            SmpCenterAlignedTopAppBar(
                visible = visible,
                title = title
            )
        }
    ) {
        appState.systemUiController.setSystemBarsColor(color = MaterialTheme.colorScheme.surface)

        appState.navController.enableOnBackPressed(appState.drawerState.isClosed)

        LaunchedEffect(key1 = Unit) {
            mainViewModel.snackBarEvent.collect { event ->
                event.let {
                    appState.snackbarHostState.showSnackbar(it.message, it.actionLabel)
                }
            }
        }
        SmpBottomDrawer(
            state = appState.drawerState,
            scope = appState.coroutineScope,
            gesturesEnabled = appState.drawerState.isOpen,
            drawerContent = {
                if (appState.drawerType.value == DrawerType.OPTION_LIST) {
                    DrawerItemsList(
                        items = appState.drawer.value?.itemList ?: emptyList(),
                        onItemClick = { index ->
                            onDrawerItemClick(
                                appState = appState,
                                mainViewModel = mainViewModel,
                                index = index
                            )
                        }
                    )
                } else {
                    PlayerRoute()
                }
            }
        ) {
            Box(modifier = Modifier.padding(it)) {
                Column {
                    SmpNavHost(
                        navHostController = appState.navController,
                        modifier = Modifier.weight(1f),
                        onBackPressed = appState::onBackPressed,
                        onShowMusicItemOption = {
                            mainViewModel.setCurrentInteractingUri(it)
                            appState.showDrawerByUri(it)
                        },
                        onNewPlayListButtonClick = {
                            appState.navController.navigateToNewPlayListDialog()
                        },
                        onCreateButtonClick = mainViewModel::onNewPlaylist
                    )

                    val backStackEntry by appState.currentBackStackEntry
                    val parentBackEntry = remember(backStackEntry) {
                        appState.navController.getBackStackEntry(homeRoute)
                    }
                    MiniPlayerBox(
                        playerStateViewModel = hiltViewModel(parentBackEntry),
                        onNavigateToPlayer = {
                            appState.showPlayerDrawer()
                        },
                        onToggleFavorite = mainViewModel::onToggleFavorite
                    )
                    val isNavigationBarVisible = remember(appState.currentNavDestination) {
                        !appState.isNavigationBarHide
                    }
                    SimpleMusicNavigationBar(
                        visible = isNavigationBarVisible,
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentNavDestination
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerItemsList(
    items: List<DrawerItem>,
    onItemClick: (Int) -> Unit
) {
    items.forEachIndexed { index, item ->
        val imageVector = when (val icon = item.icon) {
            is Icon.ImageVectorIcon -> {
                icon.imageVector
            }
        }
        DrawerItemView(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            icon = imageVector,
            text = item.text,
            onClick = {
                onItemClick(index)
            }
        )
        if (index != items.lastIndex) {
            Divider()
        }
    }
}

private fun onDrawerItemClick(
    appState: SimpleMusicAppState,
    mainViewModel: MainActivityViewModel,
    index: Int
) {
    val drawerItem = appState.drawer.value?.itemList?.get(index)!!
    mainViewModel.onDrawerItemClick(drawerItem)
    when (drawerItem) {
        DrawerItem.ADD_TO_PLAY_LIST -> {
            appState.navController.navigateToAddPlayListDialog(
                mainViewModel.interactingUri.value!!
            )
        }
        else -> Unit
    }
    appState.closeDrawer()
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

private fun getSnackBarPaddingBottom(
    appState: SimpleMusicAppState,
    isMusicBoxShowing: Boolean
): Dp {
    val navigationBarHeight = 80.0.dp
    val playerBoxHeight = 70.dp
    val snackBarPaddingBottom = 10.dp

    val isNavigationBarVisible = !appState.isNavigationBarHide
    return snackBarPaddingBottom + if (isNavigationBarVisible && !isMusicBoxShowing) {
        navigationBarHeight
    } else if (!isNavigationBarVisible && isMusicBoxShowing) {
        playerBoxHeight
    } else if (isNavigationBarVisible && isMusicBoxShowing) {
        navigationBarHeight + playerBoxHeight
    } else {
        0.dp
    }
}

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
