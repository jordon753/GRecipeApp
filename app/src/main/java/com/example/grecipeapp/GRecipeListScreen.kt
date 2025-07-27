package com.example.grecipeapp

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.grecipeapp.Database.GRecipeDatabase
import com.example.grecipeapp.Database.Recipe
import com.example.grecipeapp.Database.RecipeRepository
import com.example.grecipeapp.Database.RecipeViewModel
import com.example.grecipeapp.Database.RecipeViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel



@Composable
fun RecipeListScreen() {
    val context = LocalContext.current

    // Setup DAO → Repository → ViewModel
    val dao = GRecipeDatabase.getDatabase(context).recipeDao()
    val repository = RecipeRepository(dao)
    val viewModel: RecipeViewModel = viewModel(
        factory = RecipeViewModelFactory(repository)
    )

    val recipes by viewModel.allRecipes.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
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

    // Simple add recipe dialog
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
