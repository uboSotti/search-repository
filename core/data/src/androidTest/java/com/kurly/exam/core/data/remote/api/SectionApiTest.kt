package com.kurly.exam.core.data.remote.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SectionApiTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var api: SectionApi

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun getSections_returns_parsed_data_correctly() = runTest {
        // When
        val response = api.getSections(1)

        // Then
        assertNotNull(response)
        assertNotNull(response.data)
        assertEquals(5, response.data.size)
        assertEquals("함께하면 더 좋은 상품", response.data[0].title)
    }

    @Test
    fun getSectionProducts_returns_parsed_data_correctly() = runTest {
        // When
        val response = api.getSectionProducts(7)

        // Then
        assertNotNull(response)
        assertNotNull(response.data)
        assertEquals(3, response.data.size)
        assertEquals("국산 블루베리 100g (특)", response.data[0].name)
    }
}
