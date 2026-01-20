package com.kurly.exam.jnsk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kurly.exam.core.ui.theme.KurlyExamTheme
import com.kurly.exam.feature.main.MainRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Edge-to-Edge 활성화
        super.onCreate(savedInstanceState)
        setContent {
            KurlyExamTheme {
                MainRoute() // MainScreen 호출
            }
        }
    }
}
