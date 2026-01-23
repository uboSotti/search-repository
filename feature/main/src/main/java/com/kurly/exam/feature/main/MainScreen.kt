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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.kurly.exam.feature.main.components.ProductDisplayStyle
import com.kurly.exam.feature.main.components.ProductItem
import com.kurly.exam.feature.main.components.SectionHeader

// Layout Constants
private val SECTION_DIVIDER_THICKNESS = 10.dp
private val PADDING_HORIZONTAL_DEFAULT = 16.dp
private val PADDING_BOTTOM_LIST = 20.dp
private val SPACER_HEIGHT_DIVIDER = 24.dp
private val GRID_ROW_SPACING = 12.dp
private val GRID_COL_SPACING = 12.dp

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel(),
    onProductClick: (ProductUiModel) -> Unit = {}
) {
    val pagingItems = viewModel.pagingDataFlow.collectAsLazyPagingItems()
    val favoriteProductIds by viewModel.favoriteProductIds.collectAsStateWithLifecycle()
    
    val toggleFavorite = remember(viewModel) { viewModel::toggleFavorite }

    MainScreen(
        pagingItems = pagingItems,
        favoriteProductIds = favoriteProductIds,
        onToggleFavorite = toggleFavorite,
        onProductClick = onProductClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    pagingItems: LazyPagingItems<SectionUiModel>,
    favoriteProductIds: Set<Int>,
    onToggleFavorite: (Int) -> Unit,
    onProductClick: (ProductUiModel) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.feature_main_app_bar_title)) })
        }
    ) { paddingValues ->
        val isRefreshing = pagingItems.loadState.refresh is LoadState.Loading

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { pagingItems.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = PADDING_BOTTOM_LIST)
            ) {
                // 페이징 아이템 렌더링
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.sectionId }
                ) { index ->
                    val sectionModel = pagingItems[index]
                    
                    if (sectionModel != null) {
                        SectionItem(
                            sectionModel = sectionModel,
                            favoriteProductIds = favoriteProductIds,
                            onToggleFavorite = onToggleFavorite,
                            onProductClick = onProductClick,
                            isLastItem = index == pagingItems.itemCount - 1
                        )
                    }
                }

                // Append 로딩 및 에러 처리
                renderLoadStates(pagingItems)
            }
            
            // 초기 로딩 에러 화면
            if (pagingItems.loadState.refresh is LoadState.Error && pagingItems.itemCount == 0) {
                ErrorView(onRetry = { pagingItems.retry() })
            }
        }
    }
}

@Composable
private fun SectionItem(
    sectionModel: SectionUiModel,
    favoriteProductIds: Set<Int>,
    onToggleFavorite: (Int) -> Unit,
    onProductClick: (ProductUiModel) -> Unit,
    isLastItem: Boolean
) {
    Column {
        SectionHeader(title = sectionModel.title)

        when (sectionModel.type) {
            "horizontal" -> HorizontalSectionContent(
                products = sectionModel.products,
                favoriteProductIds = favoriteProductIds,
                onToggleFavorite = onToggleFavorite,
                onProductClick = onProductClick
            )

            "grid" -> GridSectionContent(
                products = sectionModel.products,
                favoriteProductIds = favoriteProductIds,
                onToggleFavorite = onToggleFavorite,
                onProductClick = onProductClick
            )

            "vertical" -> VerticalSectionContent(
                products = sectionModel.products,
                favoriteProductIds = favoriteProductIds,
                onToggleFavorite = onToggleFavorite,
                onProductClick = onProductClick
            )
        }

        if (!isLastItem) {
            SectionDivider()
        }
    }
}

@Composable
private fun HorizontalSectionContent(
    products: List<ProductUiModel>,
    favoriteProductIds: Set<Int>,
    onToggleFavorite: (Int) -> Unit,
    onProductClick: (ProductUiModel) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = PADDING_HORIZONTAL_DEFAULT),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = products, key = { it.id }) { product ->
            ProductItem(
                product = product,
                isFavorite = favoriteProductIds.contains(product.id),
                displayStyle = ProductDisplayStyle.HORIZONTAL,
                onWishClick = { onToggleFavorite(product.id) },
                onProductClick = { onProductClick(product) }
            )
        }
    }
}

@Composable
private fun GridSectionContent(
    products: List<ProductUiModel>,
    favoriteProductIds: Set<Int>,
    onToggleFavorite: (Int) -> Unit,
    onProductClick: (ProductUiModel) -> Unit
) {
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
                            isFavorite = favoriteProductIds.contains(product.id),
                            displayStyle = ProductDisplayStyle.GRID,
                            onWishClick = { onToggleFavorite(product.id) },
                            onProductClick = { onProductClick(product) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                repeat(3 - rowProducts.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun VerticalSectionContent(
    products: List<ProductUiModel>,
    favoriteProductIds: Set<Int>,
    onToggleFavorite: (Int) -> Unit,
    onProductClick: (ProductUiModel) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        products.forEach { product ->
            ProductItem(
                product = product,
                isFavorite = favoriteProductIds.contains(product.id),
                displayStyle = ProductDisplayStyle.VERTICAL,
                onWishClick = { onToggleFavorite(product.id) },
                onProductClick = { onProductClick(product) },
                modifier = Modifier.padding(horizontal = PADDING_HORIZONTAL_DEFAULT)
            )
        }
    }
}

@Composable
private fun SectionDivider() {
    Column {
        Spacer(modifier = Modifier.height(SPACER_HEIGHT_DIVIDER))
        HorizontalDivider(
            thickness = SECTION_DIVIDER_THICKNESS, 
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
        )
    }
}

// LazyListScope Extensions for LoadState (이는 LazyColumn DSL 내에서 사용되므로 유지 가능)
private fun LazyListScope.renderLoadStates(pagingItems: LazyPagingItems<*>) {
    when {
        pagingItems.loadState.append is LoadState.Loading -> {
            item { LoadingItem() }
        }
        pagingItems.loadState.append is LoadState.Error -> {
            item {
                ErrorItem(onRetry = { pagingItems.retry() })
            }
        }
    }
}

@Composable
private fun LoadingItem() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorItem(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "추가 데이터를 불러오지 못했습니다.", style = MaterialTheme.typography.bodySmall)
        Button(onClick = onRetry) { Text("재시도") }
    }
}

@Composable
private fun ErrorView(onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "데이터를 불러오는 중 오류가 발생했습니다.")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) { Text("다시 시도") }
        }
    }
}
