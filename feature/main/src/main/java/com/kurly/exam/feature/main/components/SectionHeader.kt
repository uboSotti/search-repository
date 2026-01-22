package com.kurly.exam.feature.main.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Layout Constants
private val PADDING_HORIZONTAL = 16.dp
private val PADDING_VERTICAL = 12.dp

// Typography Styles (Global Property with Composable Getter)
private val SectionHeaderStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.titleLarge.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = SectionHeaderStyle,
        color = MaterialTheme.colorScheme.onBackground,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .padding(horizontal = PADDING_HORIZONTAL, vertical = PADDING_VERTICAL)
    )
}
