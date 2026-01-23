package com.kurly.exam.jnsk.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.kurly.exam.feature.favorite.FavoriteRoute
import com.kurly.exam.feature.main.MainRoute
import com.kurly.exam.jnsk.R
import com.kurly.exam.jnsk.ui.navigation.Screen

enum class BottomTab(val title: Int) {
    MAIN(R.string.bottom_tab_main),
    FAVORITE(R.string.bottom_tab_favorite)
}

private fun showProductClickToast(context: Context, productName: String) {
    val message = context.getString(R.string.product_clicked_message, productName)
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun ExamApp(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    val mainBackStack = rememberNavBackStack(Screen.Main)
    val favoriteBackStack = rememberNavBackStack(Screen.Favorite)
    var currentTab by remember { mutableStateOf(BottomTab.MAIN) }

    val currentBackStack = when (currentTab) {
        BottomTab.MAIN -> mainBackStack
        BottomTab.FAVORITE -> favoriteBackStack
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
        bottomBar = {
            NavigationBar {
                BottomTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = {
                            currentTab = tab
                        },
                        icon = {
                            when (tab) {
                                BottomTab.MAIN -> Icon(Icons.Filled.Home, contentDescription = null)
                                BottomTab.FAVORITE -> Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = null
                                )
                            }
                        },
                        label = { Text(stringResource(tab.title)) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavDisplay(
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            backStack = currentBackStack,
            onBack = { currentBackStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Screen.Main> {
                    MainRoute(
                        onProductClick = { product ->
                            showProductClickToast(context, product.name)
                        }
                    )
                }
                entry<Screen.Favorite> {
                    FavoriteRoute(
                        onProductClick = { product ->
                            showProductClickToast(context, product.name)
                        }
                    )
                }
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}