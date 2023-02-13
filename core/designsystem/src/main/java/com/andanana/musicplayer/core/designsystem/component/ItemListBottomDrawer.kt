package com.andanana.musicplayer.core.designsystem.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andanana.musicplayer.core.designsystem.DrawerItem
import com.andanana.musicplayer.core.designsystem.icons.Icon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemListBottomDrawer(
    modifier: Modifier = Modifier,
    state: BottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed),
    scope: CoroutineScope = rememberCoroutineScope(),
    gesturesEnabled: Boolean,
    items: List<DrawerItem>,
    onItemClick: (Int) -> Unit,
    content: @Composable () -> Unit
) {
    BackHandler(enabled = state.isOpen) {
        scope.launch {
            state.close()
        }
    }
    BottomDrawer(
        modifier = modifier,
        drawerState = state,
        gesturesEnabled = gesturesEnabled,
        drawerBackgroundColor = MaterialTheme.colorScheme.surface,
        drawerShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        drawerContent = {
            Spacer(modifier = Modifier.height(10.dp))
            Spacer(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .height(3.dp)
                    .width(20.dp)
                    .background(
                        color = MaterialTheme.colorScheme.inversePrimary,
                        shape = MaterialTheme.shapes.medium
                    )
            )
            Spacer(modifier = Modifier.height(10.dp))
            items.forEachIndexed() { index, item ->
                val imageVector = when (val icon = item.icon) {
                    is Icon.ImageVectorIcon -> {
                        icon.imageVector
                    }
                }
                DrawerItemView(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    icon = imageVector,
                    text = item.text,
                    onClick = { onItemClick(index) }
                )
                Divider()
            }
        },
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawerItemView(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Icon(imageVector = icon, contentDescription = "")
            Spacer(modifier = Modifier.width(30.dp))
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun MusicItemBottomDrawerPreview() {
//    MusicPlayerTheme(darkTheme = true) {
//        val state = rememberBottomDrawerState(initialValue = BottomDrawerValue.Open)
//        ItemListBottomDrawer(
//            state = state,
//            items = listOf(
//                DrawerItem(Icons.Rounded.VapingRooms, "Text1"),
//                DrawerItem(Icons.Rounded.MailLock, "Text2")
//            ),
//            onItemClick = {}
//        ) {}
//    }
}
