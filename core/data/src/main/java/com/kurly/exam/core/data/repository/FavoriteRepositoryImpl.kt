package com.kurly.exam.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kurly.exam.core.domain.repository.FavoriteRepository
import com.kurly.exam.core.model.Product
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * DataStore를 활용하여 Product 엔티티 전체를 로컬 저장소에 저장하는 FavoriteRepository 구현체입니다.
 * Product 엔티티는 JSON 문자열로 직렬화되어 저장됩니다.
 */
class FavoriteRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json
) : FavoriteRepository {

    override fun getFavoriteProducts(): Flow<List<Product>> {
        return dataStore.data
            .map { preferences ->
                val jsonString = preferences[FAVORITE_PRODUCTS_KEY] ?: "null"
                if (jsonString == "null") {
                    emptyMap()
                } else {
                    runCatching {
                        json.decodeFromString(ProductMapSerializer, jsonString)
                    }.getOrDefault(emptyMap())
                }
            }
            .map { it.values.toList() }
    }

    override fun getFavoriteIds(): Flow<Set<Int>> {
        return dataStore.data
            .map { preferences ->
                val jsonString = preferences[FAVORITE_PRODUCTS_KEY] ?: "null"
                if (jsonString == "null") {
                    emptySet()
                } else {
                    runCatching {
                        json.decodeFromString(ProductMapSerializer, jsonString).keys
                    }.getOrDefault(emptySet()).toImmutableSet()
                }
            }
    }

    override suspend fun toggleFavorite(product: Product) {
        dataStore.edit { preferences ->
            val currentJson = preferences[FAVORITE_PRODUCTS_KEY] ?: "null"
            val currentMap: Map<Int, Product> = if (currentJson == "null") {
                emptyMap()
            } else {
                runCatching {
                    json.decodeFromString(ProductMapSerializer, currentJson)
                }.getOrDefault(emptyMap())
            }

            val newMap = if (currentMap.containsKey(product.id)) {
                currentMap - product.id
            } else {
                currentMap + (product.id to product)
            }

            preferences[FAVORITE_PRODUCTS_KEY] =
                json.encodeToString(ProductMapSerializer, newMap)
        }
    }

    companion object {
        private val FAVORITE_PRODUCTS_KEY = stringPreferencesKey("favorite_products_map")
        private val ProductMapSerializer: KSerializer<Map<Int, Product>> =
            MapSerializer(Int.serializer(), Product.serializer())
    }
}
