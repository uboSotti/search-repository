package com.kurly.android.mockserver

import com.kurly.android.mockserver.core.FileProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
internal class MockServerTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fileProvider: FileProvider

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `모든_섹션_리소스_파일_읽기_테스트`() {
        // Given: sections_1.json ~ sections_4.json
        (1..4).forEach { page ->
            val filePath = "sections/sections_$page.json"
            
            // When
            val jsonString = fileProvider.getJsonFromAsset(filePath)
            
            // Then
            assertNotNull("$filePath 파일을 읽어오지 못했습니다.", jsonString)
            assertTrue("$filePath 파일 내용이 비어있습니다.", jsonString?.isNotEmpty() == true)
        }
    }

    @Test
    fun `모든_상품_리스트_리소스_파일_읽기_테스트`() {
        // Given: section_products_1.json ~ section_products_20.json
        (1..20).forEach { id ->
            val filePath = "section/products/section_products_$id.json"
            
            // When
            val jsonString = fileProvider.getJsonFromAsset(filePath)
            
            // Then
            assertNotNull("$filePath 파일을 읽어오지 못했습니다.", jsonString)
            assertTrue("$filePath 파일 내용이 비어있습니다.", jsonString?.isNotEmpty() == true)
        }
    }
}
