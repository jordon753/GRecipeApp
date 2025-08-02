package com.example.grecipeapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grecipeapp.Database.Recipe

@Composable
fun CategoryManagementDialog(
    allRecipes: List<Recipe>,
    onRenameCategory: (old: String, new: String) -> Unit,
    onDeleteCategory: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val categories = remember(allRecipes) {
        allRecipes.mapNotNull { it.category }
            .toSet()
            .toList()
            .sorted()
    }

    var showRenameDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var categoryToRename by remember { mutableStateOf("") }
    var categoryToDelete by remember { mutableStateOf("") }
    var newCategoryName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Manage Categories") },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
        text = {
            Column {
                categories.forEach { category ->
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(category, modifier = Modifier.weight(1f))
                        TextButton(onClick = {
                            categoryToRename = category
                            newCategoryName = ""
                            showRenameDialog = true
                        }) {
                            Text("Rename")
                        }
                        TextButton(onClick = {
                            categoryToDelete = category
                            showDeleteConfirmDialog = true
                        },                                colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error)) {
                            Text("Delete")
                        }

                    }
                }
            }
        }
    )

    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text("Rename Category") },
            text = {
                Column {
                    Text("Current: $categoryToRename")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("New Category Name") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newCategoryName.isNotBlank()) {
                        onRenameCategory(categoryToRename, newCategoryName.trim())
                        showRenameDialog = false
                    }
                }) {
                    Text("Rename")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Confirm Delete") },
            text = {
                Text("Are you sure you want to delete the category \"$categoryToDelete\"?\nThis will remove the category from associated recipes.")
            },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteCategory(categoryToDelete)
                    showDeleteConfirmDialog = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}