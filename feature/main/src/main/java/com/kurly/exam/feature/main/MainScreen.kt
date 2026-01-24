package com.kurly.exam.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.kurly.exam.core.ui.component.ProductDisplayStyle
import com.kurly.exam.core.ui.component.ProductItem
import com.kurly.exam.core.ui.model.ProductUiModel
import com.kurly.exam.core.ui.theme.Dimen
import com.kurly.exam.feature.main.components.SectionHeader
import kotlinx.collections.immutable.ImmutableList

private object MainConstants {
    val SECTION_DIVIDER_THICKNESS = Dimen.Padding.ExtraMedium
    val PADDING_HORIZONTAL_DEFAULT = Dimen.Padding.Large
    val PADDING_BOTTOM_LIST = Dimen.Padding.ExtraLarge
    val SPACER_HEIGHT_DIVIDER = Dimen.Padding.ExtraLarge
    val GRID_ROW_SPACING = Dimen.Margin.ExtraMedium
    val GRID_COL_SPACING = Dimen.Margin.ExtraMedium
    const val GRID_CHUNK_SIZE = 3
    const val GRID_MAX_ITEMS = 6
}

/**
 * 메인 화면의 라우트 Composable.
 * [MainViewModel]과 상호작용하여 UI 상태를 관리하고 [MainScreen]을 표시합니다.
 *
 * @param viewModel 메인 화면의 [MainViewModel].
 * @param onProductClick 상품 아이템 클릭 시 호출되는 람다.
 */
@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel(),
    onProductClick: (ProductUiModel) -> Unit = {},
) {
    val pagingItems = viewModel.sectionUiState.collectAsLazyPagingItems()

    // viewModel의 함수를 remember로 감싸 불필요한 리컴포지션을 방지합니다.
    val toggleFavorite = remember(viewModel) { viewModel::toggleFavorite }

    MainScreen(
        pagingItems = pagingItems,
        onToggleFavorite = toggleFavorite,
        onProductClick = onProductClick
    )
}

/**
 * 메인 화면의 UI를 구성하는 Composable.
 * 섹션 및 상품 목록을 페이징하여 표시하고, 새로고침 기능을 제공합니다.
 *
 * @param pagingItems 페이징된 섹션 UI 모델.
 * @param onToggleFavorite 상품의 찜하기 상태를 토글할 때 호출되는 람다.
 * @param onProductClick 상품 아이템 클릭 시 호출되는 람다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    pagingItems: LazyPagingItems<SectionUiModel>,
    onToggleFavorite: (ProductUiModel) -> Unit,
    onProductClick: (ProductUiModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.feature_main_app_bar_title)) },
            windowInsets = WindowInsets(Dimen.Radius.None) // Edge-to-Edge 처리를 위해 WindowInsets 비활성화
        )

        val isRefreshing = pagingItems.loadState.refresh is LoadState.Loading

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { pagingItems.refresh() },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = MainConstants.PADDING_BOTTOM_LIST)
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.sectionId }) { index ->
                    val sectionModel = pagingItems[index]

                    if (sectionModel != null) {
                        SectionItem(
                            sectionModel = sectionModel,
                            onToggleFavorite = onToggleFavorite,
                            onProductClick = onProductClick,
                            isLastItem = index == pagingItems.itemCount - 1
                        )
                    }
                }

                renderLoadStates(pagingItems)
            }

            // 초기 로드 실패 시 에러 화면 표시
            if (pagingItems.loadState.refresh is LoadState.Error && pagingItems.itemCount == 0) {
                ErrorView(onRetry = { pagingItems.retry() })
            }
        }
    }
}

/**
 * 단일 섹션을 표시하는 Composable.
 * 섹션의 유형에 따라 다른 콘텐츠를 렌더링합니다.
 */
@Composable
private fun SectionItem(
    sectionModel: SectionUiModel,
    onToggleFavorite: (ProductUiModel) -> Unit,
    onProductClick: (ProductUiModel) -> Unit,
    isLastItem: Boolean
) {
    Column {
        SectionHeader(title = sectionModel.title)

        when (sectionModel.type) {
            "horizontal" -> HorizontalSectionContent(
                products = sectionModel.products,
                onToggleFavorite = onToggleFavorite,
                onProductClick = onProductClick
            )

            "grid" -> GridSectionContent(
                products = sectionModel.products,
                onToggleFavorite = onToggleFavorite,
                onProductClick = onProductClick
            )

            "vertical" -> VerticalSectionContent(
                products = sectionModel.products,
                onToggleFavorite = onToggleFavorite,
                onProductClick = onProductClick
            )
        }

        // 마지막 아이템이 아닐 경우에만 구분선을 추가합니다.
        if (!isLastItem) {
            SectionDivider()
        }
    }
}

/** 가로 스크롤 섹션의 콘텐츠를 표시합니다. */
@Composable
private fun HorizontalSectionContent(
    products: ImmutableList<ProductUiModel>,
    onToggleFavorite: (ProductUiModel) -> Unit,
    onProductClick: (ProductUiModel) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = MainConstants.PADDING_HORIZONTAL_DEFAULT),
        horizontalArrangement = Arrangement.spacedBy(MainConstants.GRID_ROW_SPACING)
    ) {
        items(items = products, key = { it.id }) { product ->
            ProductItem(
                product = product,
                isFavorite = product.isFavorite,
                displayStyle = ProductDisplayStyle.HORIZONTAL,
                onWishClick = { onToggleFavorite(product) },
                onProductClick = { onProductClick(product) },
            )
        }
    }
}

/** 그리드 섹션의 콘텐츠를 표시합니다. (최대 6개 상품) */
@Composable
private fun GridSectionContent(
    products: ImmutableList<ProductUiModel>,
    onToggleFavorite: (ProductUiModel) -> Unit,
    onProductClick: (ProductUiModel) -> Unit
) {
    val displayProducts = products.take(MainConstants.GRID_MAX_ITEMS)
    Column(
        modifier = Modifier.padding(horizontal = MainConstants.PADDING_HORIZONTAL_DEFAULT),
        verticalArrangement = Arrangement.spacedBy(MainConstants.GRID_COL_SPACING)
    ) {
        displayProducts.chunked(MainConstants.GRID_CHUNK_SIZE).forEach { rowProducts ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MainConstants.GRID_ROW_SPACING)
            ) {
                rowProducts.forEach { product ->
                    Box(modifier = Modifier.weight(1f)) {
                        ProductItem(
                            product = product,
                            isFavorite = product.isFavorite,
                            displayStyle = ProductDisplayStyle.GRID,
                            onWishClick = { onToggleFavorite(product) },
                            onProductClick = { onProductClick(product) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
                // 그리드 행의 빈 공간을 채우기 위한 Spacer
                repeat(MainConstants.GRID_CHUNK_SIZE - rowProducts.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

/** 세로 리스트 섹션의 콘텐츠를 표시합니다. */
@Composable
private fun VerticalSectionContent(
    products: ImmutableList<ProductUiModel>,
    onToggleFavorite: (ProductUiModel) -> Unit,
    onProductClick: (ProductUiModel) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimen.Margin.ExtraMedium)) {
        products.forEach { product ->
            ProductItem(
                product = product,
                isFavorite = product.isFavorite,
                displayStyle = ProductDisplayStyle.VERTICAL,
                onWishClick = { onToggleFavorite(product) },
                onProductClick = { onProductClick(product) },
                modifier = Modifier.padding(horizontal = MainConstants.PADDING_HORIZONTAL_DEFAULT),
            )
        }
    }
}

/** 섹션 사이에 표시되는 구분선 Composable. */
@Composable
private fun SectionDivider() {
    Column {
        Spacer(modifier = Modifier.height(MainConstants.SPACER_HEIGHT_DIVIDER))
        HorizontalDivider(
            thickness = MainConstants.SECTION_DIVIDER_THICKNESS,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
        )
    }
}

/**
 * Paging의 LoadState에 따라 로딩, 에러 UI를 렌더링하는 확장 함수.
 */
private fun LazyListScope.renderLoadStates(pagingItems: LazyPagingItems<*>) {
    when (pagingItems.loadState.append) {
        is LoadState.Loading -> {
            item { LoadingItem() }
        }

        is LoadState.Error -> {
            item {
                ErrorItem(onRetry = { pagingItems.retry() })
            }
        }

        is LoadState.NotLoading -> {}
    }
}

/** 추가 데이터 로딩 시 표시되는 Composable. */
@Composable
private fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimen.Padding.Large), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/** 추가 데이터 로딩 실패 시 표시되는 Composable. */
@Composable
private fun ErrorItem(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimen.Padding.Large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.feature_main_load_more_error), style = MaterialTheme.typography.bodySmall)
        Button(onClick = onRetry) { Text(stringResource(R.string.feature_main_retry)) }
    }
}

/** 초기 데이터 로딩 실패 시 표시되는 전체 화면 에러 Composable. */
@Composable
private fun ErrorView(onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.feature_main_initial_load_error))
            Spacer(modifier = Modifier.height(Dimen.Padding.Medium))
            Button(onClick = onRetry) { Text(stringResource(R.string.feature_main_retry)) }
        }
    }
}
