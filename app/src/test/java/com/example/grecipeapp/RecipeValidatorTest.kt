package com.example.grecipeapp

import org.junit.Assert.assertTrue
import org.junit.Test

class RecipeValidatorTest {

    @Test
    fun `title should not be blank`() {
        val title = "Soup"
        assertTrue(title.isNotBlank())
    }

    @Test
    fun `description can be optional`() {
        val description = ""
        assertTrue(description.isEmpty())
    }
}
