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
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            KurlyExamTheme {
                ExamApp()
            }
        }
    }
}
