package com.gordon.grecipeapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.gordon.grecipeapp.Database.Recipe
import org.junit.Rule
import org.junit.Test

class RecipeDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun editButton_isVisible() {
        val recipe = Recipe(id = 1, title = "Noodles", description = "Spicy", category = "Lunch")
        composeTestRule.setContent {
            RecipeDetailScreen(
                recipe = recipe,
                onBack = {},
                onDelete = {},
                onUpdate = {},
                categorySuggestions = listOf("Lunch", "Dinner")
            )
        }

        composeTestRule
            .onNodeWithText("Edit")
            .assertIsDisplayed()
    }
}
