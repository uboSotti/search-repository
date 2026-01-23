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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.kurly.exam.feature.favorite.FavoriteRoute
import com.kurly.exam.feature.main.MainRoute
import com.kurly.exam.feature.main.MainViewModel
import com.kurly.exam.jnsk.R
import com.kurly.exam.jnsk.ui.navigation.Screen

/**
 * 하단 탭 메뉴 열거형 클래스
 *
 * @property title 각 탭의 제목 리소스 ID
 */
enum class BottomTab(val title: Int) {
    /** 메인 화면 탭 */
    MAIN(R.string.bottom_tab_main),

    /** 찜하기 화면 탭 */
    FAVORITE(R.string.bottom_tab_favorite)
}

/**
 * 상품 클릭 시 토스트 메시지를 표시합니다.
 *
 * @param context 컨텍스트
 * @param productName 상품 이름
 */
private fun showProductClickToast(context: Context, productName: String) {
    val message = context.getString(R.string.product_clicked_message, productName)
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

/**
 * 앱의 메인 화면을 구성하는 Composable 함수입니다.
 * 하단 네비게이션 바와 각 탭에 해당하는 화면을 포함합니다.
 */
@Composable
fun ExamApp(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    // 각 탭의 백스택을 관리합니다.
    val mainBackStack = rememberNavBackStack(Screen.Main)
    val favoriteBackStack = rememberNavBackStack(Screen.Favorite)
    var currentTab by remember { mutableStateOf(BottomTab.MAIN) }

    // 현재 선택된 탭에 따라 보여줄 백스택을 결정합니다.
    val currentBackStack = when (currentTab) {
        BottomTab.MAIN -> mainBackStack
        BottomTab.FAVORITE -> favoriteBackStack
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
        bottomBar = {
            NavigationBar {
                // 하단 네비게이션 바의 각 아이템을 구성합니다.
                BottomTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
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
        // 현재 백스택에 따라 화면을 표시합니다.
        NavDisplay(
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(
                    removeViewModelStoreOnPop = {
                        false //FIXME Backstack 의 전환 인 경우에는 삭제되면 안됨
                    }
                )
            ),
            backStack = currentBackStack,
            onBack = { currentBackStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Screen.Main> {
                    val viewModel = hiltViewModel<MainViewModel>()
                    MainRoute(
                        viewModel = viewModel,
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
