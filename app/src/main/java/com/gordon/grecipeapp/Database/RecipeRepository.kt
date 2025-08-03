package com.gordon.grecipeapp.Database

import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val dao: RecipeDao) {
    val allRecipes: Flow<List<Recipe>> = dao.getAllRecipes()

    suspend fun insert(recipe:Recipe) = dao.insertRecipe(recipe)
    suspend fun update(recipe: Recipe) = dao.updateRecipe(recipe)
    suspend fun delete(recipe: Recipe) = dao.deleteRecipe(recipe)
    suspend fun getAllRecipes(): List<Recipe> = dao.getAll()


}
