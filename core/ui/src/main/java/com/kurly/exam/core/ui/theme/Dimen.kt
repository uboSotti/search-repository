package com.kurly.exam.core.ui.theme

import androidx.compose.ui.unit.dp

/**
 * 앱 전체에서 사용될 간격 및 크기 값들을 정의하는 디자인 시스템 객체.
 *
 * 사용 예시:
 * ```
 * Modifier.padding(Dimen.Padding.Medium)
 * ```
 */
object Dimen {
    object Padding {
        val Tiny = 2.dp
        val Small = 4.dp
        val Medium = 8.dp
        val ExtraMedium = 12.dp
        val Large = 16.dp
        val ExtraLarge = 24.dp
        val DoubleExtraLarge = 32.dp
    }

    object Margin {
        val Tiny = 2.dp
        val Small = 4.dp
        val Medium = 8.dp
        val ExtraMedium = 12.dp
        val Large = 16.dp
        val ExtraLarge = 24.dp
        val DoubleExtraLarge = 32.dp
    }

    object Radius {
        val None = 0.dp
        val Small = 4.dp
        val Medium = 8.dp
        val Large = 16.dp
    }
    
    object Size {
        val IconSmall = 18.dp
        val IconMedium = 24.dp
        val IconLarge = 32.dp
        val SmallImage = 48.dp
        val MediumImage = 72.dp
        val LargeImage = 96.dp
        val ProductItemHorizontalWidth = 150.dp
    }
}