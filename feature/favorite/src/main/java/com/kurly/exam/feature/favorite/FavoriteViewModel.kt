package com.kurly.exam.feature.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kurly.exam.core.domain.usecase.ObserveFavoriteProductsUseCase
import com.kurly.exam.core.domain.usecase.ToggleFavoriteUseCase
import com.kurly.exam.core.model.Product
import com.kurly.exam.core.ui.model.ProductUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    observeFavoriteProductsUseCase: ObserveFavoriteProductsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    val favoriteProducts: StateFlow<ImmutableList<ProductUiModel>> =
        observeFavoriteProductsUseCase()
            .map { products -> products.map { it.toUiModel() }.toImmutableList() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = kotlinx.collections.immutable.persistentListOf()
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
