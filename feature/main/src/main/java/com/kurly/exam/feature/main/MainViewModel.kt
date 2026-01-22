package com.kurly.exam.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kurly.exam.core.domain.model.Product
import com.kurly.exam.core.domain.model.Section
import com.kurly.exam.core.domain.usecase.GetSectionProductsUseCase
import com.kurly.exam.core.domain.usecase.GetSectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI 전용 모델 정의
data class ProductUiModel(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val originalPrice: Int,
    val discountedPrice: Int?,
    val isSoldOut: Boolean,
    val isFavorite: Boolean = false // 찜하기 상태 (로컬 관리)
) {
    // 할인율 계산 로직 등 UI 편의 기능
    val discountRate: Int?
        get() = if (discountedPrice != null && originalPrice > 0) {
            ((originalPrice - discountedPrice).toDouble() / originalPrice * 100).toInt()
        } else {
            null
        }
}

data class SectionUiModel(
    val section: Section,
    val products: List<ProductUiModel>
)

data class MainUiState(
    val isLoading: Boolean = false,
    val sectionModels: List<SectionUiModel> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSectionsUseCase: GetSectionsUseCase,
    private val getSectionProductsUseCase: GetSectionProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState(isLoading = true))
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    // 찜한 상품 ID 목록 (메모리 캐시 - TODO: 추후 DataStore 연동 필요)
    private val favoriteProductIds = mutableSetOf<Int>()

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            // 1. 섹션 목록 가져오기
            getSectionsUseCase(page = 1)
                .onSuccess { sections ->
                    // 2. 각 섹션별 상품 목록 병렬 요청
                    val sectionUiModels = sections.map { section ->
                        async {
                            var products: List<ProductUiModel> = emptyList()
                            getSectionProductsUseCase(section.id)
                                .onSuccess { domainProducts ->
                                    products = domainProducts.map { it.toUiModel() }
                                }
                                .onFailure {
                                    // 개별 섹션 로딩 실패 시 빈 리스트 처리 또는 에러 처리 정책 결정
                                    // 현재는 빈 리스트로 진행
                                }
                            SectionUiModel(section = section, products = products)
                        }
                    }.awaitAll()

                    _uiState.update {
                        it.copy(isLoading = false, sectionModels = sectionUiModels)
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = throwable.message)
                    }
                }
        }
    }

    // 찜하기 토글
    fun toggleFavorite(productId: Int) {
        if (favoriteProductIds.contains(productId)) {
            favoriteProductIds.remove(productId)
        } else {
            favoriteProductIds.add(productId)
        }
        
        // TODO: DataStore에 favoriteProductIds 저장 로직 추가 필요

        // UI 상태 업데이트 (deep copy 필요)
        _uiState.update { currentState ->
            val updatedSections = currentState.sectionModels.map { sectionModel ->
                val updatedProducts = sectionModel.products.map { product ->
                    if (product.id == productId) {
                        product.copy(isFavorite = favoriteProductIds.contains(productId))
                    } else {
                        product
                    }
                }
                sectionModel.copy(products = updatedProducts)
            }
            currentState.copy(sectionModels = updatedSections)
        }
    }

    private fun Product.toUiModel(): ProductUiModel {
        return ProductUiModel(
            id = id,
            name = name,
            imageUrl = image,
            originalPrice = originalPrice,
            discountedPrice = discountedPrice,
            isSoldOut = isSoldOut,
            isFavorite = favoriteProductIds.contains(id)
        )
    }
}
