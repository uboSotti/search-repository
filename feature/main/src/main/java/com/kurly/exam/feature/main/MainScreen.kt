package com.kurly.exam.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kurly.exam.core.domain.model.Section
import com.kurly.exam.core.ui.theme.KurlyExamTheme
import com.kurly.exam.feature.main.components.ProductItem

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MainScreen(uiState = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: MainUiState
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Kurly Exam") })
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.errorMessage)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.sections, key = { it.id }) { section ->
                    ProductSection(section = section)
                    HorizontalDivider(color = Color.LightGray)
                }
            }
        }
    }
}

@Composable
fun ProductSection(
    modifier: Modifier = Modifier,
    section: Section
) {
    Column(modifier) {
        SectionTitle(
            modifier = Modifier.padding(16.dp),
            title = section.title
        )
        // TODO: section.type에 따라 다른 Composable 표시 (Horizontal, Grid, Vertical)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(5) { // TODO: GetSectionProductsUseCase 호출하여 실제 데이터로 교체
                ProductItem(
                    modifier = Modifier.width(150.dp),
                    imageUrl = "https://picsum.photos/id/${it}/150/200",
                    title = "[샐러딩] 레디믹스 스탠다드 150g",
                    originalPrice = 8000,
                    discountedPrice = 6200,
                    isWished = false,
                    onWishClick = {}
                )
            }
        }
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
        MainScreen(uiState = MainUiState(sections = listOf(Section("Title", 1, "horizontal", ""))))
    }
}
