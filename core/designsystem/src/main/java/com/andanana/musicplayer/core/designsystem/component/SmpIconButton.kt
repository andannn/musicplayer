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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
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
        Icon(modifier = Modifier.scale(1.5f), imageVector = imageVector, contentDescription = null)
    }
}

@Composable
fun SmpSubIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    scale: Float = 1.5f
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(modifier = Modifier.scale(scale), imageVector = imageVector, contentDescription = null)
    }
}

@Composable
fun SmpSubIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    painter: Painter
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(painter = painter, contentDescription = null)
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
