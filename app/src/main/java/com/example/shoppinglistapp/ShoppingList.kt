package com.example.shoppinglistapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ShoppingListItem(
    val id: Int,
    val name: String,
    val quantity: Int,
    var isEditing: Boolean = false
) {
    fun displayText(): String {
        return "$name x$quantity"
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp(){
    var listItems by remember { mutableStateOf(listOf<ShoppingListItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = !showDialog},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(listItems.size) { index ->
                val item = listItems[index]
                Text(
                    text = item.displayText(),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        if (showDialog) {
            AlertDialog (
                onDismissRequest = {showDialog = false}
            ) {
                Text(text = "Hello World")
            }
        }
    }
}