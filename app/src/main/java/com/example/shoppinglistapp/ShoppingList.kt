package com.example.shoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingListItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)


@Composable
fun ShoppingListApp() {
    var listItems by remember { mutableStateOf(listOf<ShoppingListItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = !showDialog },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(listItems) { item ->
                if (item.isEditing) {
                    ItemEditor(
                        item = item,
                        onEditComplete = { editedName, editedQuantity ->
                            listItems = listItems.map { it.copy(isEditing = false) }
                            val editedItem = listItems.find { it.id == item.id }
                            editedItem?.let {
                                it.name = editedName
                                it.quantity = editedQuantity
                            }
                        }
                    )
                } else {
                    ShoppingList(item = item, onEditClick = {
                        listItems = listItems.map {
                            it.copy(isEditing = it.id == item.id && !it.isEditing)
                        }
                    }, onDeleteClick = {
                        listItems = listItems - item
                    })
                }
            }
        }
    }
    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false }, confirmButton = {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    if (name.isEmpty() || quantity == 0) {
                        return@Button
                    } else {
                        listItems += ShoppingListItem(
                            id = listItems.size + 1,
                            name = name,
                            quantity = quantity
                        )
                        showDialog = false
                        name = ""
                        quantity = 0
                    }
                }) {
                    Text(text = "Add")
                }
                Button(onClick = {
                    showDialog = !showDialog
                }) {
                    Text(text = "Cancel")
                }
            }
        }, title = {
            Text(
                text = "Add Item"
            )
        }, text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = { Text("Item Name") },
                    singleLine = true,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    value = quantity.toString(),
                    onValueChange = {
                        quantity = it.toIntOrNull() ?: 0
                    },
                    label = { Text("Quantity") },
                    singleLine = true,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
            }
        })
    }
}

@Composable
fun ShoppingList(
    item: ShoppingListItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, color = Color.Cyan),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty : ${item.quantity}", modifier = Modifier.padding(8.dp))
        IconButton(onClick = onEditClick) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
        }
        IconButton(onClick = onDeleteClick) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}


@Composable
fun ItemEditor(
    item: ShoppingListItem,
    onEditComplete: (String, Int) -> Unit,
) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableIntStateOf(item.quantity) }
    var isEditing by remember { mutableStateOf(item.isEditing) }
    Column {
        OutlinedTextField(
            value = editedName,
            onValueChange = {
                editedName = it
            },
            label = { Text("Item Name") },
            singleLine = true,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentSize()
        )
        OutlinedTextField(
            value = editedQuantity.toString(),
            onValueChange = {
                editedQuantity = it.toIntOrNull() ?: 0
            },
            label = { Text("Quantity") },
            singleLine = true,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentSize()
        )
        Button(onClick = {
            isEditing = false
            onEditComplete(editedName, editedQuantity)
        }) {
            Text(text = "Save")
        }
    }
}
