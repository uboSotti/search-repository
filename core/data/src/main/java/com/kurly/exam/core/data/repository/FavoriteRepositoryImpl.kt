package com.kurly.exam.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.kurly.exam.core.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : FavoriteRepository {

    private val FAVORITE_PRODUCT_IDS_KEY = stringSetPreferencesKey("favorite_product_ids")

    override fun getFavoriteIds(): Flow<Set<Int>> {
        return dataStore.data.map { preferences ->
            preferences[FAVORITE_PRODUCT_IDS_KEY]?.map { it.toInt() }?.toSet() ?: emptySet()
        }
    }

    override suspend fun toggleFavorite(productId: Int) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_PRODUCT_IDS_KEY] ?: emptySet()
            val productIdStr = productId.toString()
            if (currentFavorites.contains(productIdStr)) {
                preferences[FAVORITE_PRODUCT_IDS_KEY] = currentFavorites - productIdStr
            } else {
                preferences[FAVORITE_PRODUCT_IDS_KEY] = currentFavorites + productIdStr
            }
        }
    }
}