package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.MovieRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("MovieRulz", appName)
    }

    @Test
    fun `curated movie catalog has entries`() {
        val catalog = MovieRepository.curatedMovies
        assertTrue(catalog.isNotEmpty())
        val pushpa = catalog.firstOrNull { it.id == "pushpa-2" }
        assertEquals("Pushpa 2: The Rule", pushpa?.title)
    }
}
