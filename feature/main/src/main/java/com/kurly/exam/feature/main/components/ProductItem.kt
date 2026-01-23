package com.kurly.exam.feature.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kurly.exam.core.ui.theme.DiscountRed
import com.kurly.exam.core.ui.theme.KurlyExamTheme
import com.kurly.exam.feature.main.ProductUiModel
import com.kurly.exam.feature.main.R

enum class ProductDisplayStyle {
    VERTICAL,
    HORIZONTAL,
    GRID
}

// Layout Constants
private const val ASPECT_RATIO_VERTICAL = 1.5f
private const val ASPECT_RATIO_DEFAULT = 0.75f
private val WIDTH_HORIZONTAL_ITEM = 150.dp
private val PADDING_SMALL = 4.dp
private val PADDING_MEDIUM = 8.dp
private val PADDING_LARGE = 16.dp

// Resource Constants
private val ICON_WISH_ON = R.drawable.ic_btn_heart_on
private val ICON_WISH_OFF = R.drawable.ic_btn_heart_off

// Typography Styles
private val DiscountRateStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        color = DiscountRed
    )

private val DiscountPriceStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )

private val OriginalPriceStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.bodySmall.copy(
        textDecoration = TextDecoration.LineThrough,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

private val SinglePriceStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: ProductUiModel,
    isFavorite: Boolean,
    displayStyle: ProductDisplayStyle,
    onWishClick: () -> Unit,
    onProductClick: () -> Unit
) {
    val isVertical = displayStyle == ProductDisplayStyle.VERTICAL
    val imageAspectRatio = if (isVertical) ASPECT_RATIO_VERTICAL else ASPECT_RATIO_DEFAULT
    val titleMaxLines = if (isVertical) 1 else 2

    Column(
        modifier = modifier
            .width(if (displayStyle == ProductDisplayStyle.HORIZONTAL) WIDTH_HORIZONTAL_ITEM else Modifier.fillMaxWidth().run { Float.NaN.dp })
            .clickable(onClick = onProductClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(imageAspectRatio)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            IconButton(
                onClick = onWishClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(PADDING_SMALL)
            ) {
                Icon(
                    painter = painterResource(id = if (isFavorite) ICON_WISH_ON else ICON_WISH_OFF),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }

        Spacer(modifier = Modifier.height(PADDING_MEDIUM))

        Text(
            text = product.name,
            maxLines = titleMaxLines,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = PADDING_SMALL)
        )

        Spacer(modifier = Modifier.height(PADDING_SMALL))

        PriceSection(
            originalPrice = product.originalPrice,
            discountedPrice = product.discountedPrice,
            discountRate = product.discountRate,
            isVertical = isVertical,
            modifier = Modifier.padding(horizontal = PADDING_SMALL)
        )
        
        Spacer(modifier = Modifier.height(PADDING_LARGE))
    }
}

@Composable
private fun PriceSection(
    originalPrice: Int,
    discountedPrice: Int?,
    discountRate: Int?,
    isVertical: Boolean,
    modifier: Modifier = Modifier
) {
    if (discountedPrice != null && discountRate != null) {
        val discountRateText = stringResource(id = R.string.feature_main_discount_rate_format, discountRate)
        val discountedPriceText = stringResource(id = R.string.feature_main_price_format, discountedPrice)
        val originalPriceText = stringResource(id = R.string.feature_main_price_format, originalPrice)

        if (isVertical) {
            Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
                Text(text = discountRateText, style = DiscountRateStyle)
                Spacer(modifier = Modifier.width(PADDING_SMALL))
                Text(text = discountedPriceText, style = DiscountPriceStyle)
                Spacer(modifier = Modifier.width(PADDING_SMALL))
                Text(text = originalPriceText, style = OriginalPriceStyle)
            }
        } else {
            Column(modifier = modifier) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = discountRateText, style = DiscountRateStyle)
                    Spacer(modifier = Modifier.width(PADDING_SMALL))
                    Text(text = discountedPriceText, style = DiscountPriceStyle)
                }
                Text(text = originalPriceText, style = OriginalPriceStyle)
            }
        }
    } else {
        Text(
            text = stringResource(id = R.string.feature_main_price_format, originalPrice),
            style = SinglePriceStyle,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductItemPreview() {
    KurlyExamTheme {
        Row {
            ProductItem(
                product = ProductUiModel(
                    id = 1,
                    name = "[샐러딩] 레디믹스 스탠다드 150g",
                    imageUrl = "",
                    originalPrice = 8000,
                    discountedPrice = 6200,
                    isSoldOut = false
                ),
                isFavorite = true,
                displayStyle = ProductDisplayStyle.HORIZONTAL,
                onWishClick = {},
                onProductClick = {}
            )
        }
    }
}
