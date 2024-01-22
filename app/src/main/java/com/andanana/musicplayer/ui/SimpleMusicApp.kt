package com.andanana.musicplayer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.andanana.musicplayer.MainActivityViewModel
import com.andanana.musicplayer.core.designsystem.DrawerItem
import com.andanana.musicplayer.core.designsystem.component.DrawerItemView
import com.andanana.musicplayer.core.designsystem.icons.Icon
import com.andanana.musicplayer.feature.player.MiniPlayerBox
import com.andanana.musicplayer.navigation.SmpNavHost

private const val TAG = "SimpleMusicApp"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimpleMusicApp(appState: SimpleMusicAppState = rememberSimpleMusicAppState()) {
    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) {
            "No view model store owner"
        }
    val mainViewModel: MainActivityViewModel =
        remember(viewModelStoreOwner) {
            ViewModelProvider(viewModelStoreOwner)[MainActivityViewModel::class.java]
        }

    appState.systemUiController.setSystemBarsColor(color = MaterialTheme.colorScheme.surface)

    appState.navController.enableOnBackPressed(appState.drawerState.isClosed)

    LaunchedEffect(key1 = Unit) {
        mainViewModel.snackBarEvent.collect { event ->
            event.let {
                appState.snackbarHostState.showSnackbar(it.message, it.actionLabel)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SmpNavHost(
            modifier = Modifier.fillMaxWidth().weight(1f),
            navHostController = appState.navController,
            onBackPressed = appState::onBackPressed,
            onShowMusicItemOption = {
                mainViewModel.setCurrentInteractingUri(it)
                appState.showDrawerByUri(it)
            },
            onNewPlayListButtonClick = {
//                            appState.navController.navigateToNewPlayListDialog()
            },
            onCreateButtonClick = {
            },
        )

        MiniPlayerBox(
            modifier =
                Modifier.navigationBarsPadding()
                    .background(MaterialTheme.colorScheme.surface),
            onNavigateToPlayer = {
                appState.showPlayerDrawer()
            },
            onToggleFavorite = {},
        )
    }
}

@Composable
private fun DrawerItemsList(
    items: List<DrawerItem>,
    onItemClick: (Int) -> Unit,
) {
    items.forEachIndexed { index, item ->
        val imageVector =
            when (val icon = item.icon) {
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
            },
        )
        if (index != items.lastIndex) {
            Divider()
        }
    }
}

private fun onDrawerItemClick(
    appState: SimpleMusicAppState,
    mainViewModel: MainActivityViewModel,
    index: Int,
) {
    val drawerItem = appState.drawer.value?.itemList?.get(index)!!
    mainViewModel.onDrawerItemClick(drawerItem)
    when (drawerItem) {
        DrawerItem.ADD_TO_PLAY_LIST -> {
//            appState.navController.navigateToAddPlayListDialog(
//                mainViewModel.interactingUri.value!!,
//            )
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
    title: String,
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                Text(
                    text = title,
                )
            },
        )
    }
}

private fun getSnackBarPaddingBottom(
    appState: SimpleMusicAppState,
    isMusicBoxShowing: Boolean,
): Dp {
    val navigationBarHeight = 80.0.dp
    val playerBoxHeight = 70.dp
    val snackBarPaddingBottom = 10.dp

    val isNavigationBarVisible = !appState.isNavigationBarHide
    return snackBarPaddingBottom +
        if (isNavigationBarVisible && !isMusicBoxShowing) {
            navigationBarHeight
        } else if (!isNavigationBarVisible && isMusicBoxShowing) {
            playerBoxHeight
        } else if (isNavigationBarVisible && isMusicBoxShowing) {
            navigationBarHeight + playerBoxHeight
        } else {
            0.dp
        }
}
