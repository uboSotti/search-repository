package com.kurly.exam.feature.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kurly.exam.core.domain.usecase.ObserveFavoriteProductsUseCase
import com.kurly.exam.core.domain.usecase.ToggleFavoriteUseCase
import com.kurly.exam.core.ui.model.ProductUiModel
import com.kurly.exam.core.ui.model.toDomain
import com.kurly.exam.core.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 찜하기 화면의 ViewModel.
 * 찜한 상품 목록을 관찰하고, 찜하기 상태를 변경하는 비즈니스 로직을 처리합니다.
 *
 * @param observeFavoriteProductsUseCase 찜한 상품 목록을 관찰하는 유즈케이스.
 * @param toggleFavoriteUseCase 상품의 찜 상태를 토글하는 유즈케이스.
 */
@HiltViewModel
class FavoriteViewModel @Inject constructor(
    observeFavoriteProductsUseCase: ObserveFavoriteProductsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    /**
     * 찜한 상품 목록을 UI 모델([ProductUiModel])의 리스트로 변환하여 [StateFlow]로 제공합니다.
     * 이 Flow는 UI가 구독하는 동안 활성화되며, 5초의 타임아웃을 가집니다.
     */
    val favoriteProductUiState: StateFlow<ImmutableList<ProductUiModel>> =
        observeFavoriteProductsUseCase()
            .map { products -> products.map { it.toUiModel(true) }.toImmutableList() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = kotlinx.collections.immutable.persistentListOf()
            )

    /**
     * 상품의 찜 상태를 토글합니다.
     *
     * @param productUiModel 찜 상태를 변경할 상품의 UI 모델.
     */
    fun toggleFavorite(productUiModel: ProductUiModel) {
        viewModelScope.launch {
            toggleFavoriteUseCase(productUiModel.toDomain())
        }
    }
}
