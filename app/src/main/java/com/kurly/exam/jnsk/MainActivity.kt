package com.kurly.exam.jnsk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kurly.exam.core.ui.theme.KurlyExamTheme
import com.kurly.exam.jnsk.ui.ExamApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Edge-to-Edge 활성화
        super.onCreate(savedInstanceState)
        setContent {
            KurlyExamTheme {
                // Navigation 3.0 기반의 화면 구성을 시작하는 ExamApp Composable을 호출합니다.
                ExamApp()
            }
        }
    }
}
