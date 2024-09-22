package com.andannn.melodify.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CircleBorderImage(
    model: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        modifier =
            modifier
                .clip(shape = CircleShape)
                .border(
                    shape = CircleShape,
                    border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primary),
                ),
        model = model,
        contentDescription = "",
    )
}
