package com.example.grecipeapp

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.grecipeapp.Database.GRecipeDatabase
import com.example.grecipeapp.Database.Recipe
import com.example.grecipeapp.Database.RecipeRepository
import com.example.grecipeapp.Database.RecipeViewModel
import com.example.grecipeapp.Database.RecipeViewModelFactory


@Composable
fun RecipeListScreen() {
    val context = LocalContext.current
    val dao = GRecipeDatabase.getDatabase(context).recipeDao()
    val repository = RecipeRepository(dao)
    val viewModel: RecipeViewModel = viewModel(
        factory = RecipeViewModelFactory(repository)
    )
    val recipes by viewModel.allRecipes.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }

    selectedRecipe?.let { recipe ->
        RecipeDetailScreen(
            recipe = recipe,
            onBack = { selectedRecipe = null }, // Back resets selection
            onDelete = {
                viewModel.delete(it)
                selectedRecipe = null
            },
            onUpdate = {
                viewModel.update(it)
                selectedRecipe = null
            }
        )
        return
    }

    Scaffold(
        topBar = { GRecipeTopAppBar(title = "Recipe List") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Recipe List",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(recipes) { recipe ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedRecipe = recipe } // 👉 Handle click
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = recipe.title, style = MaterialTheme.typography.titleMedium)
                            recipe.description?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = it, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddRecipeDialog(
            onDismiss = { showDialog = false },
            onAddRecipe = { title, description ->
                val newRecipe = Recipe(title = title, description = description)
                viewModel.insert(newRecipe)
                Toast.makeText(context, "Recipe added", Toast.LENGTH_SHORT).show()
                showDialog = false
            }
        )
    }
}


@Composable
fun AddRecipeDialog(onDismiss: () -> Unit, onAddRecipe: (String, String?) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                if (title.isNotBlank()) {
                    onAddRecipe(title, description.ifBlank { null })
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add Recipe") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (optional)") }
                )
            }
        }
    )
}

