package com.kurly.exam.feature.main.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kurly.exam.core.ui.theme.DiscountRed
import com.kurly.exam.core.ui.theme.KurlyExamTheme
import com.kurly.exam.feature.main.R

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    imageUrl: String,
    title: String,
    originalPrice: Int,
    discountedPrice: Int? = null,
    isWished: Boolean,
    onWishClick: () -> Unit
) {
    val context = LocalContext.current
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f)
                .background(Color.LightGray)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .crossfade(true)
                    .data(imageUrl)
                    .build(),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            // 찜하기 버튼
            IconButton(
                onClick = onWishClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = if (isWished) R.drawable.ic_btn_heart_on else R.drawable.ic_btn_heart_off),
                    contentDescription = "찜하기",
                    tint = Color.Unspecified // Drawable 자체 색상 사용
                )
            }

            // 장바구니 버튼
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "장바구니 담기",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (discountedPrice != null) {
            val discountRate =
                ((originalPrice - discountedPrice).toDouble() / originalPrice * 100).toInt()
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$discountRate%",
                    color = DiscountRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "%,d원".format(discountedPrice),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Text(
                text = "%,d원".format(originalPrice),
                textDecoration = TextDecoration.LineThrough,
                color = Color.Gray,
                fontSize = 14.sp
            )
        } else {
            Text(
                text = "%,d원".format(originalPrice),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 150)
@Composable
private fun ProductItemPreview() {
    KurlyExamTheme {
        ProductItem(
            imageUrl = "",
            title = "[샐러딩] 레디믹스 스탠다드 150g",
            originalPrice = 8000,
            discountedPrice = 6200,
            isWished = true,
            onWishClick = {}
        )
    }
}
