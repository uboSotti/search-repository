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

private val GRID_SPACING = 16.dp

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
            windowInsets = WindowInsets()
        )
        
        if (favoriteProducts.isEmpty()) {
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
                        isFavorite = true,
                        displayStyle = ProductDisplayStyle.GRID,
                        onWishClick = { onToggleFavorite(product) },
                        onProductClick = { onProductClick(product) },
                    )
                }
            }
        }
    }
}
