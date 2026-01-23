package com.kurly.exam.core.ui.component

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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kurly.exam.core.ui.R
import com.kurly.exam.core.ui.model.ProductUiModel
import com.kurly.exam.core.ui.theme.DiscountRed

/**
 * 상품 정보를 표시하는 UI 컴포넌트입니다.
 * 상품의 이미지, 이름, 가격 정보 및 찜하기 상태를 표시하며, 다양한 디스플레이 스타일에 따라 레이아웃이 변경됩니다.
 *
 * @param modifier Composable에 적용할 Modifier.
 * @param product 표시할 상품의 데이터 모델 ([ProductUiModel]).
 * @param isFavorite 현재 상품의 찜하기 상태.
 * @param displayStyle 상품 표시 스타일 ([ProductDisplayStyle]).
 * @param onWishClick 찜하기 버튼 클릭 시 호출되는 람다.
 * @param onProductClick 상품 아이템 클릭 시 호출되는 람다.
 */
@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: ProductUiModel,
    isFavorite: Boolean,
    displayStyle: ProductDisplayStyle,
    onWishClick: () -> Unit,
    onProductClick: () -> Unit,
) {
    val wishIconOnId = R.drawable.ic_btn_heart_on
    val wishIconOffId = R.drawable.ic_btn_heart_off

    val isVertical = displayStyle == ProductDisplayStyle.VERTICAL
    val imageAspectRatio = if (isVertical) ASPECT_RATIO_VERTICAL else ASPECT_RATIO_DEFAULT
    val titleMaxLines = if (isVertical) 1 else 2

    Column(
        modifier = modifier
            .width(
                if (displayStyle == ProductDisplayStyle.HORIZONTAL) WIDTH_HORIZONTAL_ITEM else Modifier.fillMaxWidth().run { Float.NaN.dp }
            )
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
                    painter = painterResource(id = if (isFavorite) wishIconOnId else wishIconOffId),
                    contentDescription = stringResource(R.string.wish_button_content_description),
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

/**
 * 가격 정보를 표시하는 내부 컴포저블.
 * 할인 여부와 표시 스타일에 따라 다른 레이아웃을 보여줍니다.
 */
@Composable
private fun PriceSection(
    originalPrice: Int,
    discountedPrice: Int?,
    discountRate: Int?,
    isVertical: Boolean,
    modifier: Modifier = Modifier
) {
    val priceFormatId = R.string.ui_price_format
    val discountRateFormatId = R.string.ui_discount_rate_format

    if (discountedPrice != null && discountRate != null) {
        val discountRateText = stringResource(id = discountRateFormatId, discountRate)
        val discountedPriceText = stringResource(id = priceFormatId, discountedPrice)
        val originalPriceText = stringResource(id = priceFormatId, originalPrice)

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
            text = stringResource(id = priceFormatId, originalPrice),
            style = SinglePriceStyle,
            modifier = modifier
        )
    }
}

/**
 * 상품 아이템의 표시 스타일을 정의하는 열거형 클래스.
 */
enum class ProductDisplayStyle {
    /** 세로 리스트 스타일 */
    VERTICAL,
    /** 가로 리스트 스타일 */
    HORIZONTAL,
    /** 그리드 스타일 */
    GRID
}

// Layout Constants
private const val ASPECT_RATIO_VERTICAL = 1.5f
private const val ASPECT_RATIO_DEFAULT = 0.75f
private val WIDTH_HORIZONTAL_ITEM = 150.dp
private val PADDING_SMALL = 4.dp
private val PADDING_MEDIUM = 8.dp
private val PADDING_LARGE = 16.dp

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
