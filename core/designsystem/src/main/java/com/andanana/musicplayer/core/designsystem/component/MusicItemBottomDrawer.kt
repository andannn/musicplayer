package com.andanana.musicplayer.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme

private enum class DrawerItem(
    val icon: Int,
    val text: String
) {
    ADD_TO_FAVORITE(
        icon = 0,
        text = "Save to Favorite"
    ),
    ADD_TO_PLAY_LIST(
        icon = 0,
        text = "Save to PlayList"
    ),
    Share(
        icon = 0,
        text = "Share"
    ),
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MusicItemBottomDrawer(
    modifier: Modifier = Modifier
) {
    val state = rememberBottomDrawerState(initialValue = BottomDrawerValue.Open)
    BottomDrawer(
        modifier = modifier,
        drawerState = state,
        drawerContent = {
            DrawerItem.values().forEach { item ->
                DrawerItem(item = item)
                Divider()
            }
        },
        content = {
            Surface() {
                Text(text = "AAAAAAAAAAAAAAa")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawerItem(
    modifier: Modifier = Modifier,
    item: DrawerItem,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row {
//        Icon(painter = painterResource(id = item.icon), contentDescription = "")
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = item.text)
        }
    }
}

@Preview
@Composable
private fun MusicItemBottomDrawerPreview() {
    MusicPlayerTheme {
        MusicItemBottomDrawer()
    }
}
