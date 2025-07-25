package com.example.grecipeapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.grecipeapp.ui.theme.GRecipeAppTheme

data class Recipe(val title: String, val description: String)

val sampleRecipes = listOf(
    Recipe("Spaghetti", "Simple spaghetti with tomato sauce."),
    Recipe("Pancakes", "Fluffy pancakes with maple syrup."),
    Recipe("Salad", "Healthy green salad with olive oil.")
)

@Composable
fun GRecipeListScreen() {
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }

    if (selectedRecipe == null) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(sampleRecipes) { recipe ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { selectedRecipe = recipe }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = recipe.title, style = MaterialTheme.typography.titleLarge)
                        Text(text = recipe.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    } else {
        RecipeDetailScreen(recipe = selectedRecipe!!) {
            selectedRecipe = null
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeListPreview() {
    GRecipeAppTheme {
        GRecipeListScreen()
    }
}