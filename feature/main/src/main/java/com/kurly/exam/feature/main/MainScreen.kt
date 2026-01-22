package com.kurly.exam.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kurly.exam.feature.main.components.ProductDisplayStyle
import com.kurly.exam.feature.main.components.ProductItem
import com.kurly.exam.feature.main.components.SectionHeader

// 상수 정의
private val SECTION_DIVIDER_THICKNESS = 10.dp
private val PADDING_HORIZONTAL_DEFAULT = 16.dp
private val PADDING_VERTICAL_DEFAULT = 12.dp
private val PADDING_BOTTOM_LIST = 20.dp
private val SPACER_HEIGHT_DIVIDER = 24.dp
private val GRID_ROW_SPACING = 12.dp
private val GRID_COL_SPACING = 12.dp

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel(),
    onProductClick: (ProductUiModel) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    val refreshData = remember(viewModel) { viewModel::refreshData }
    val toggleFavorite = remember(viewModel) { viewModel::toggleFavorite }

    MainScreen(
        uiState = uiState,
        onRefresh = refreshData,
        onToggleFavorite = toggleFavorite,
        onProductClick = onProductClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: MainUiState,
    onRefresh: () -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onProductClick: (ProductUiModel) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.feature_main_app_bar_title)) })
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.errorMessage != null && !uiState.isLoading && uiState.sectionModels.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.errorMessage)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = PADDING_BOTTOM_LIST)
                ) {
                    uiState.sectionModels.forEachIndexed { index, sectionModel ->
                        val sectionId = sectionModel.section.id
                        
                        sectionHeader(title = sectionModel.section.title, key = "header-$sectionId")

                        when (sectionModel.section.type) {
                            "horizontal" -> horizontalSection(
                                sectionId = sectionId,
                                products = sectionModel.products,
                                onToggleFavorite = onToggleFavorite,
                                onProductClick = onProductClick
                            )

                            "grid" -> gridSection(
                                sectionId = sectionId,
                                products = sectionModel.products,
                                onToggleFavorite = onToggleFavorite,
                                onProductClick = onProductClick
                            )

                            "vertical" -> verticalSection(
                                sectionId = sectionId,
                                products = sectionModel.products,
                                onToggleFavorite = onToggleFavorite,
                                onProductClick = onProductClick
                            )
                        }

                        if (index != uiState.sectionModels.lastIndex) {
                            sectionDivider(key = "divider-$sectionId")
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.sectionHeader(title: String, key: Any) {
    item(key = key) {
        SectionHeader(title = title)
    }
}

private fun LazyListScope.horizontalSection(
    sectionId: Int,
    products: List<ProductUiModel>,
    onToggleFavorite: (Int) -> Unit,
    onProductClick: (ProductUiModel) -> Unit
) {
    item(key = "horizontal-$sectionId") {
        LazyRow(
            contentPadding = PaddingValues(horizontal = PADDING_HORIZONTAL_DEFAULT),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = products,
                key = { it.id }
            ) { product ->
                ProductItem(
                    product = product,
                    displayStyle = ProductDisplayStyle.HORIZONTAL,
                    onWishClick = { onToggleFavorite(product.id) },
                    onProductClick = { onProductClick(product) }
                )
            }
        }
    }
}

private fun LazyListScope.gridSection(
    sectionId: Int,
    products: List<ProductUiModel>,
    onToggleFavorite: (Int) -> Unit,
    onProductClick: (ProductUiModel) -> Unit
) {
    item(key = "grid-$sectionId") {
        val displayProducts = products.take(6)
        
        Column(
            modifier = Modifier.padding(horizontal = PADDING_HORIZONTAL_DEFAULT),
            verticalArrangement = Arrangement.spacedBy(GRID_COL_SPACING)
        ) {
            displayProducts.chunked(3).forEach { rowProducts ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(GRID_ROW_SPACING)
                ) {
                    rowProducts.forEach { product ->
                        Box(modifier = Modifier.weight(1f)) {
                            ProductItem(
                                product = product,
                                displayStyle = ProductDisplayStyle.GRID,
                                onWishClick = { onToggleFavorite(product.id) },
                                onProductClick = { onProductClick(product) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    
                    repeat(3 - rowProducts.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

private fun LazyListScope.verticalSection(
    sectionId: Int,
    products: List<ProductUiModel>,
    onToggleFavorite: (Int) -> Unit,
    onProductClick: (ProductUiModel) -> Unit
) {
    items(
        items = products,
        key = { "vertical-$sectionId-${it.id}" }
    ) { product ->
        ProductItem(
            product = product,
            displayStyle = ProductDisplayStyle.VERTICAL,
            onWishClick = { onToggleFavorite(product.id) },
            onProductClick = { onProductClick(product) },
            modifier = Modifier.padding(horizontal = PADDING_HORIZONTAL_DEFAULT, vertical = PADDING_VERTICAL_DEFAULT)
        )
    }
}

private fun LazyListScope.sectionDivider(key: Any) {
    item(key = key) {
        Spacer(modifier = Modifier.height(SPACER_HEIGHT_DIVIDER))
        HorizontalDivider(
            thickness = SECTION_DIVIDER_THICKNESS, 
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
        )
    }
}
