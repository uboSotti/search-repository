package com.kurly.exam.feature.main

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kurly.exam.core.domain.usecase.GetSectionsPagedUseCase
import com.kurly.exam.core.domain.usecase.ObserveFavoriteProductIdsUseCase
import com.kurly.exam.core.domain.usecase.ToggleFavoriteUseCase
import com.kurly.exam.core.model.Product
import com.kurly.exam.core.ui.model.ProductUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 메인 화면의 섹션 UI를 나타내는 데이터 클래스.
 *
 * @property sectionId 섹션의 고유 식별자.
 * @property title 섹션의 제목.
 * @property type 섹션의 유형 (e.g., "horizontal", "grid", "vertical").
 * @property products 섹션에 포함된 상품 목록.
 */
@Immutable
data class SectionUiModel(
    val sectionId: Int,
    val title: String,
    val type: String,
    val products: ImmutableList<ProductUiModel>
)

/**
 * 메인 화면의 ViewModel.
 * 페이징된 섹션 데이터를 로드하고, 찜하기 상태를 관리합니다.
 *
 * @param getSectionsPagedUseCase 페이징된 섹션 데이터를 가져오는 유즈케이스.
 * @param observeFavoriteProductIdsUseCase 찜한 상품 ID 목록을 관찰하는 유즈케이스.
 * @param toggleFavoriteUseCase 상품의 찜 상태를 토글하는 유즈케이스.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    getSectionsPagedUseCase: GetSectionsPagedUseCase,
    observeFavoriteProductIdsUseCase: ObserveFavoriteProductIdsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    /**
     * 페이징된 섹션 데이터를 UI 모델로 변환하여 [Flow]로 제공합니다.
     * 데이터는 [viewModelScope] 내에서 캐시됩니다.
     */
    val pagingDataFlow: Flow<PagingData<SectionUiModel>> = getSectionsPagedUseCase()
        .map { pagingData ->
            pagingData.map { domainModel ->
                SectionUiModel(
                    sectionId = domainModel.section.id,
                    title = domainModel.section.title,
                    type = domainModel.section.type,
                    products = domainModel.products.map { it.toUiModel() }.toImmutableList()
                )
            }
        }
        .cachedIn(viewModelScope)

    /**
     * 찜한 상품 ID 목록을 [StateFlow]로 제공합니다.
     * UI가 구독하는 동안 활성화되며, 5초의 타임아웃을 가집니다.
     */
    val favoriteProductIds: StateFlow<ImmutableSet<Int>> = observeFavoriteProductIdsUseCase()
        .map { it.toImmutableSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = kotlinx.collections.immutable.persistentSetOf()
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

    /**
     * [Product] 도메인 모델을 [ProductUiModel]로 변환합니다.
     */
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

    /**
     * [ProductUiModel]을 [Product] 도메인 모델로 변환합니다.
     */
    private fun ProductUiModel.toDomain(): Product {
        return Product(
            id = id,
            name = name,
            image = imageUrl,
            originalPrice = originalPrice,
            discountedPrice = discountedPrice,
            isSoldOut = isSoldOut
        )
    }
}
