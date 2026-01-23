package com.kurly.exam.feature.main

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kurly.exam.core.domain.model.Product
import com.kurly.exam.core.domain.usecase.GetSectionsPagedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// UI 전용 모델 정의 (Recomposition 최적화를 위해 Immutable 선언)
@Immutable
data class ProductUiModel(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val originalPrice: Int,
    val discountedPrice: Int?,
    val isSoldOut: Boolean
) {
    val discountRate: Int?
        get() = if (discountedPrice != null && originalPrice > 0) {
            ((originalPrice - discountedPrice).toDouble() / originalPrice * 100).toInt()
        } else {
            null
        }
}

@Immutable
data class SectionUiModel(
    val sectionId: Int,
    val title: String,
    val type: String,
    val products: List<ProductUiModel>
)

@HiltViewModel
class MainViewModel @Inject constructor(
    getSectionsPagedUseCase: GetSectionsPagedUseCase
) : ViewModel() {

    // Paging Data Flow
    val pagingDataFlow: Flow<PagingData<SectionUiModel>> = getSectionsPagedUseCase()
        .map { pagingData ->
            pagingData.map { domainModel ->
                SectionUiModel(
                    sectionId = domainModel.section.id,
                    title = domainModel.section.title,
                    type = domainModel.section.type,
                    products = domainModel.products.map { it.toUiModel() }
                )
            }
        }
        .cachedIn(viewModelScope)

    // 찜한 상품 ID 목록
    private val _favoriteProductIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteProductIds: StateFlow<Set<Int>> = _favoriteProductIds.asStateFlow()

    // 찜하기 토글
    fun toggleFavorite(productId: Int) {
        _favoriteProductIds.update { currentSet ->
            if (currentSet.contains(productId)) {
                currentSet - productId
            } else {
                currentSet + productId
            }
        }
    }

    private fun Product.toUiModel(): ProductUiModel {
        return ProductUiModel(
            id = id,
            name = name,
            imageUrl = image,
            originalPrice = originalPrice,
            discountedPrice = discountedPrice,
            isSoldOut = isSoldOut
        )
    }
}
