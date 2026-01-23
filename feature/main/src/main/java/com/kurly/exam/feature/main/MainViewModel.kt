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

@Immutable
data class SectionUiModel(
    val sectionId: Int,
    val title: String,
    val type: String,
    val products: ImmutableList<ProductUiModel>
)

@HiltViewModel
class MainViewModel @Inject constructor(
    getSectionsPagedUseCase: GetSectionsPagedUseCase,
    observeFavoriteProductIdsUseCase: ObserveFavoriteProductIdsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

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

    val favoriteProductIds: StateFlow<ImmutableSet<Int>> = observeFavoriteProductIdsUseCase()
        .map { it.toImmutableSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = kotlinx.collections.immutable.persistentSetOf()
        )

    fun toggleFavorite(productUiModel: ProductUiModel) {
        viewModelScope.launch {
            toggleFavoriteUseCase(productUiModel.toDomain())
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
