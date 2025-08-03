@file:OptIn(ExperimentalMaterial3Api::class)

package com.gordon.grecipeapp

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun GRecipeTopAppBar(title: String) {
    TopAppBar(
        title = {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(24.dp)
                )
                Text(text = title)
            }
        }
    )
}
