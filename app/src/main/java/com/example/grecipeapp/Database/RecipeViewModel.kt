package com.example.grecipeapp.Database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {
    val allRecipes: LiveData<List<Recipe>> = repository.allRecipes.asLiveData()

    fun insert(recipe: Recipe) = viewModelScope.launch {
        repository.insert(recipe)
    }
    fun update(recipe: Recipe) = viewModelScope.launch {
        repository.update(recipe)
    }

    fun delete(recipe: Recipe) = viewModelScope.launch {
        repository.delete(recipe)
    }

    fun renameCategory(old: String, new: String) = viewModelScope.launch {
        val updatedRecipes = repository.getAllRecipes().filter { it.category == old }
            .map { it.copy(category = new) }
        updatedRecipes.forEach { repository.update(it) }
    }

    fun deleteCategory(category: String) = viewModelScope.launch {
        val updatedRecipes = repository.getAllRecipes().filter { it.category == category }
            .map { it.copy(category = null) }
        updatedRecipes.forEach { repository.update(it) }
    }


}

class RecipeViewModelFactory(private val repository: RecipeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}