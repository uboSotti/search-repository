package com.kurly.exam.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kurly.exam.core.ui.theme.KurlyExamTheme
import com.kurly.exam.feature.main.components.ProductItem

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel()
) {
    MainScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // 임시로 화면 내에서 상태 관리
    val wishedItems = remember { mutableStateMapOf<Int, Boolean>() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Kurly Exam") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp) // 섹션 간 구분
        ) {
            // Horizontal Section
            item {
                ProductSection(title = "Horizontal Section") {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(5) { index ->
                            val isWished = wishedItems[index] ?: false
                            ProductItem(
                                modifier = Modifier.width(150.dp),
                                imageUrl = "https://picsum.photos/id/${index}/150/200",
                                title = "[샐러딩] 레디믹스 스탠다드 150g",
                                originalPrice = 8000,
                                discountedPrice = 6200,
                                isWished = isWished,
                                onWishClick = { wishedItems[index] = !isWished }
                            )
                        }
                    }
                }
            }

            item { HorizontalDivider(color = Color.LightGray) }

            // Grid Section
            item {
                ProductSection(title = "Grid Section") {
                    // Grid content will be placed here
                    Text("Grid Content", modifier = Modifier.padding(horizontal = 16.dp))
                }
            }

            item { HorizontalDivider(color = Color.LightGray) }

            // Vertical Section
            item {
                ProductSection(title = "Vertical Section") {
                    // Vertical list content will be placed here
                    Text("Vertical Content", modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
fun ProductSection(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        SectionTitle(
            modifier = Modifier.padding(16.dp),
            title = title
        )
        content()
    }
}

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "더보기",
            tint = Color.Gray
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    KurlyExamTheme {
        MainScreen()
    }
}
