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
import com.kurly.exam.core.ui.model.ProductUiModel
import com.kurly.exam.core.ui.model.toDomain
import com.kurly.exam.core.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
     * 찜한 상품 ID 목록을 관리하는 private [StateFlow].
     */
    private val favoriteProductIds: StateFlow<Set<Int>> = observeFavoriteProductIdsUseCase()
        .map { it.toImmutableSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    /**
     * combine 연산자에서 여러 번 구독할 수 있도록 PagingData 스트림을 캐시합니다.
     */
    private val sectionsPagingData = getSectionsPagedUseCase().cachedIn(viewModelScope)

    /**
     * 페이징된 섹션 데이터와 찜하기 상태를 결합하여 UI에 노출하는 [Flow].
     * 찜 상태가 변경되면, PagingData 스트림이 새로운 UI 모델로 업데이트됩니다.
     */
    val sectionUiState: Flow<PagingData<SectionUiModel>> =
        combine(
            sectionsPagingData,
            favoriteProductIds
        ) { pagingData, favoriteIds ->
            pagingData.map { domainModel ->
                SectionUiModel(
                    sectionId = domainModel.section.id,
                    title = domainModel.section.title,
                    type = domainModel.section.type,
                    products = domainModel.products.map { product ->
                        product.toUiModel(isFavorite = favoriteIds.contains(product.id))
                    }.toImmutableList()
                )
            }
        }


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
