package com.example.grecipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.grecipeapp.ui.theme.GRecipeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showHome by remember { mutableStateOf(true) }

            GRecipeAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    if (showHome) {
                        HomeScreen(onStartClick = { showHome = false })
                    } else {
                        RecipeListScreen()
                    }
                }
            }
        }
    }
}
