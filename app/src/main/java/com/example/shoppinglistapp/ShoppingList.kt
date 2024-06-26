package com.example.shoppinglistapp

import android.Manifest
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController

data class ShoppingListItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false,
    var address: String,
)


@Composable
fun ShoppingListApp(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    navController: NavController,
    context: Context,
    address: String
) {
    var listItems by remember { mutableStateOf(listOf<ShoppingListItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(0) }
    val requestPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                locationUtils.getLocationData(viewModel)
            } else {
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if (rationaleRequired) {
                    Toast.makeText(context, "Location Permission is required", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(
                        context,
                        "Location Permission is denied, please allow it in settings",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = !showDialog },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add a Shopping Item")
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
                                it.address = address
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
                            quantity = quantity,
                            address = address
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
                Button(onClick = {
                    if (locationUtils.hasPermission(context)) {
                        locationUtils.getLocationData(viewModel)
                        navController.navigate("locationSelection"){
                            this.launchSingleTop = true
                        }
                    } else {
                        requestPermission.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }) {
                    Text(text = "Address")
                }
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
        Column(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
        ) {
            Row {
                Text(text = item.name, modifier = Modifier.padding(8.dp))
                Text(text = "Qty : ${item.quantity}", modifier = Modifier.padding(8.dp))
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Address")
                Log.d("res1", "Address : ${item.address}")
                Text(text = item.address, modifier = Modifier.padding(8.dp))
            }
        }
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
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
