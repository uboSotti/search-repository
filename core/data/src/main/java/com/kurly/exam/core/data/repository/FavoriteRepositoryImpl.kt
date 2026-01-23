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
 * [FavoriteRepository]의 구현체.
 * DataStore를 사용하여 찜한 상품 목록을 로컬에 저장하고 관리합니다.
 * 상품 데이터는 전체 [Product] 객체를 JSON 형태로 직렬화하여 저장합니다.
 *
 * @param dataStore [Preferences]를 저장하는 DataStore 인스턴스.
 * @param json 직렬화/역직렬화를 위한 [Json] 인스턴스.
 */
class FavoriteRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json
) : FavoriteRepository {

    /**
     * 찜한 모든 상품의 목록을 [Flow]로 반환합니다.
     * DataStore에 저장된 JSON 문자열을 [Product] 객체 리스트로 역직렬화합니다.
     */
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

    /**
     * 찜한 모든 상품의 ID를 [Set] 형태로 [Flow]로 반환합니다.
     */
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

    /**
     * 상품의 찜 상태를 토글합니다.
     * 상품이 이미 찜 목록에 있으면 제거하고, 없으면 추가합니다.
     *
     * @param product 찜 상태를 변경할 [Product] 객체.
     */
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
        /** DataStore에 찜한 상품 목록을 저장하기 위한 키 */
        private val FAVORITE_PRODUCTS_KEY = stringPreferencesKey("favorite_products_map")

        /** [Map<Int, Product>] 형태의 데이터를 직렬화/역직렬화하기 위한 Serializer */
        private val ProductMapSerializer: KSerializer<Map<Int, Product>> =
            MapSerializer(Int.serializer(), Product.serializer())
    }
}
