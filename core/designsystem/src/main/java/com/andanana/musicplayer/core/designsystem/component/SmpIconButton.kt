package com.andanana.musicplayer.core.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SmpMainIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors()
    ) {
        Icon(imageVector = imageVector, contentDescription = null)
    }
}

@Composable
fun SmpSubIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(imageVector = imageVector, contentDescription = null)
    }
}

@Preview
@Composable
private fun SmpMainIconButtonPreview() {
    MaterialTheme {
        SmpMainIconButton(
            imageVector = Icons.Rounded.PlayArrow,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun SmpSubIconButtonPreview() {
    MaterialTheme {
        SmpSubIconButton(
            imageVector = Icons.Rounded.PlayArrow,
            onClick = {}
        )
    }
}
