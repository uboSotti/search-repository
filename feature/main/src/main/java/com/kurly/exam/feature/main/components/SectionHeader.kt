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

/** 가로 방향 패딩 */
private val PADDING_HORIZONTAL = 16.dp
/** 세로 방향 패딩 */
private val PADDING_VERTICAL = 12.dp

/** 섹션 헤더의 텍스트 스타일 */
private val SectionHeaderStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.titleLarge.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

/**
 * 메인 화면의 각 섹션 헤더를 표시하는 Composable.
 *
 * @param title 표시할 섹션의 제목.
 * @param modifier Composable에 적용할 Modifier.
 */
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
