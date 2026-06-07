package com.example.tourismguide.util

import android.content.Context
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.util.Locale
import org.junit.Assert.assertEquals

class LocaleHelperTest {

    @MockK
    lateinit var context: Context

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testSetLocaleToEnglish() {
        val mockContext = mockk<Context>()
        every { mockContext.resources } returns mockk()
        every { mockContext.resources.configuration } returns mockk()
        every { mockContext.createConfigurationContext(any()) } returns mockContext

        val originalLocale = Locale.getDefault()
        try {
            val newContext = LocaleHelper.setLocale(mockContext, "en")
            assertEquals(newContext, mockContext)
        } finally {
            Locale.setDefault(originalLocale)
        }
    }

    @Test
    fun testSetLocaleToFrench() {
        val mockContext = mockk<Context>()
        every { mockContext.resources } returns mockk()
        every { mockContext.resources.configuration } returns mockk()
        every { mockContext.createConfigurationContext(any()) } returns mockContext

        val originalLocale = Locale.getDefault()
        try {
            val newContext = LocaleHelper.setLocale(mockContext, "fr")
            assertEquals(newContext, mockContext)
        } finally {
            Locale.setDefault(originalLocale)
        }
    }

    @Test
    fun testSetLocaleToArabic() {
        val mockContext = mockk<Context>()
        every { mockContext.resources } returns mockk()
        every { mockContext.resources.configuration } returns mockk()
        every { mockContext.createConfigurationContext(any()) } returns mockContext

        val originalLocale = Locale.getDefault()
        try {
            val newContext = LocaleHelper.setLocale(mockContext, "ar")
            assertEquals(newContext, mockContext)
        } finally {
            Locale.setDefault(originalLocale)
        }
    }
}
