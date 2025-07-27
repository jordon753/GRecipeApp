package com.example.grecipeapp.Database

import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val dao: RecipeDao) {
    val allRecipes: Flow<List<Recipe>> = dao.getAllRecipes()

    suspend fun insert(recipe:Recipe) = dao.insertRecipe(recipe)
}
