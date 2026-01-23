package com.kurly.exam.feature.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kurly.exam.core.ui.component.ProductDisplayStyle
import com.kurly.exam.core.ui.component.ProductItem
import com.kurly.exam.core.ui.model.ProductUiModel

/** 그리드 아이템 간의 간격 */
private val GRID_SPACING = 16.dp

/**
 * 찜하기 화면의 라우트 Composable.
 * [FavoriteViewModel]과 상호작용하여 UI 상태를 관리하고 [FavoriteScreen]을 표시합니다.
 *
 * @param viewModel 찜하기 화면의 [FavoriteViewModel].
 * @param onProductClick 상품 아이템 클릭 시 호출되는 람다.
 */
@Composable
fun FavoriteRoute(
    viewModel: FavoriteViewModel = hiltViewModel(),
    onProductClick: (ProductUiModel) -> Unit = {}
) {
    val favoriteProducts by viewModel.favoriteProducts.collectAsStateWithLifecycle()

    FavoriteScreen(
        favoriteProducts = favoriteProducts,
        onToggleFavorite = viewModel::toggleFavorite,
        onProductClick = onProductClick
    )
}

/**
 * 찜하기 화면의 UI를 구성하는 Composable.
 * 찜한 상품 목록을 그리드 형태로 표시합니다.
 *
 * @param favoriteProducts 표시할 찜한 상품 목록.
 * @param onToggleFavorite 상품의 찜하기 상태를 토글할 때 호출되는 람다.
 * @param onProductClick 상품 아이템 클릭 시 호출되는 람다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteScreen(
    favoriteProducts: List<ProductUiModel>,
    onToggleFavorite: (ProductUiModel) -> Unit,
    onProductClick: (ProductUiModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.feature_favorite_app_bar_title)) },
            windowInsets = WindowInsets(0.dp) // Edge-to-Edge 처리를 위해 WindowInsets 비활성화
        )

        if (favoriteProducts.isEmpty()) {
            // 찜한 상품이 없을 때 표시할 화면
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.feature_favorite_empty_list),
                )
            }
        } else {
            // 찜한 상품 목록을 3열 그리드로 표시
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(GRID_SPACING),
                verticalArrangement = Arrangement.spacedBy(GRID_SPACING),
                horizontalArrangement = Arrangement.spacedBy(GRID_SPACING)
            ) {
                items(items = favoriteProducts, key = { it.id }) { product ->
                    ProductItem(
                        product = product,
                        isFavorite = true, // 찜하기 화면에서는 항상 true
                        displayStyle = ProductDisplayStyle.GRID,
                        onWishClick = { onToggleFavorite(product) },
                        onProductClick = { onProductClick(product) },
                    )
                }
            }
        }
    }
}
