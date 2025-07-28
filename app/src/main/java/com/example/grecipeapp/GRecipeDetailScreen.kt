//package com.example.grecipeapp
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.grecipeapp.Database.Recipe
//
//
//@Composable
//fun RecipeDetailScreen(recipe: Recipe, onBack: () -> Unit) {
//    Scaffold(
//        topBar = { GRecipeTopAppBar(title = "Recipe Details") }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .padding(16.dp)
//        ) {
//            Text(
//                text = recipe.title,
//                style = MaterialTheme.typography.headlineMedium
//            )
//            Spacer(modifier = Modifier.height(12.dp))
//            Text(
//                text = recipe.description ?: "No description",
//                style = MaterialTheme.typography.bodyLarge
//            )
//            Spacer(modifier = Modifier.height(24.dp))
//            Button(onClick = onBack) {
//                Text("Back")
//            }
//        }
//    }
//}


package com.example.grecipeapp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.grecipeapp.Database.Recipe

@Composable
fun RecipeDetailScreen(
    recipe: Recipe,
    onBack: () -> Unit,
    onDelete: (Recipe) -> Unit,
    onUpdate: (Recipe) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var updatedTitle by remember { mutableStateOf(recipe.title) }
    var updatedDescription by remember { mutableStateOf(recipe.description ?: "") }

    val context = LocalContext.current

    Scaffold(
        topBar = { GRecipeTopAppBar(title = "Recipe Details") }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (isEditing) {
                OutlinedTextField(
                    value = updatedTitle,
                    onValueChange = { updatedTitle = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = updatedDescription,
                    onValueChange = { updatedDescription = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = {
                        if (updatedTitle.isNotBlank()) {
                            onUpdate(
                                recipe.copy(
                                    title = updatedTitle,
                                    description = updatedDescription.ifBlank { null }
                                )
                            )
                            Toast.makeText(context, "Recipe updated", Toast.LENGTH_SHORT).show()
                            isEditing = false
                        } else {
                            Toast.makeText(context, "Title can't be empty", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("Save")
                    }

                    OutlinedButton(onClick = { isEditing = false }) {
                        Text("Cancel")
                    }
                }
            } else {
                Text(recipe.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(12.dp))
                Text(recipe.description ?: "No description", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { isEditing = true }) {
                        Text("Edit")
                    }
                    OutlinedButton(onClick = {
                        onDelete(recipe)
                        Toast.makeText(context, "Recipe deleted", Toast.LENGTH_SHORT).show()
                        onBack()
                    }) {
                        Text("Delete")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(onClick = onBack) {
                    Text("Back")
                }
            }
        }
    }
}
