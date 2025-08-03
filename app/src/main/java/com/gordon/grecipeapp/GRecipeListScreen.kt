package com.gordon.grecipeapp

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gordon.grecipeapp.Database.GRecipeDatabase
import com.gordon.grecipeapp.Database.Recipe
import com.gordon.grecipeapp.Database.RecipeRepository
import com.gordon.grecipeapp.Database.RecipeViewModel
import com.gordon.grecipeapp.Database.RecipeViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen() {
    val context = LocalContext.current
    val dao = GRecipeDatabase.getDatabase(context).recipeDao()
    val repository = RecipeRepository(dao)
    val viewModel: RecipeViewModel = viewModel(factory = RecipeViewModelFactory(repository))
    val allRecipes by viewModel.allRecipes.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }
    var selectedCategory by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf(false) }
    var showManageDialog by remember { mutableStateOf(false) }
    var showDeleteRecipeDialog by remember { mutableStateOf(false) }
    var recipeToDelete by remember { mutableStateOf<Recipe?>(null) }


    // Dynamically collect all categories from current recipes
    val categories = remember(allRecipes) {
        val catList = allRecipes.mapNotNull { it.category }.toSet().toMutableList()
        catList.sort()
        listOf("All") + catList
    }

    val filteredRecipes = if (selectedCategory == "All") allRecipes
    else allRecipes.filter {
        it.category == selectedCategory
    }

    selectedRecipe?.let { recipe ->
        RecipeDetailScreen(
            recipe = recipe,
            onBack = { selectedRecipe = null },
            onDelete = {
                recipeToDelete = it
                showDeleteRecipeDialog = true
            },
            onUpdate = {
                viewModel.update(it)
                selectedRecipe = null
            },
            categorySuggestions = categories.filter { it != "All" }
        )
        return
    }

    Scaffold(
        topBar = { GRecipeTopAppBar(title = "GRecipeApp") },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            TextButton(onClick = { showManageDialog = true }) {
                Text("Manage Categories")
            }
            Text("Filter by Category", style = MaterialTheme.typography.titleMedium)


            // Category Filter Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(filteredRecipes) { recipe ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedRecipe = recipe }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(recipe.title, style = MaterialTheme.typography.titleMedium)
                            recipe.description?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(it, style = MaterialTheme.typography.bodyMedium)
                            }
                            recipe.category?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Category: $it", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    }
    if (showDeleteRecipeDialog && recipeToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteRecipeDialog = false
                recipeToDelete = null
            },
            title = { Text("Delete Recipe") },
            text = { Text("Are you sure you want to delete \"${recipeToDelete?.title}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    recipeToDelete?.let { viewModel.delete(it) }
                    Toast.makeText(context, "Recipe deleted", Toast.LENGTH_SHORT).show()
                    showDeleteRecipeDialog = false
                    recipeToDelete = null
                    selectedRecipe = null
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteRecipeDialog = false
                    recipeToDelete = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }
    if (showManageDialog) {
        CategoryManagementDialog(
            allRecipes = allRecipes,
            onRenameCategory = { old, new -> viewModel.renameCategory(old, new) },
            onDeleteCategory = { toDelete -> viewModel.deleteCategory(toDelete) },
            onDismiss = { showManageDialog = false }
        )
    }

    if (showDialog) {
        AddRecipeDialog(
            onDismiss = { showDialog = false },
            onAdd = { title, description, category ->
                viewModel.insert(Recipe(title = title, description = description, category = category))
                Toast.makeText(context, "Recipe added", Toast.LENGTH_SHORT).show()
                showDialog = false
            },
            categorySuggestions = categories.filter{it!="All"}
        )

    }
}

