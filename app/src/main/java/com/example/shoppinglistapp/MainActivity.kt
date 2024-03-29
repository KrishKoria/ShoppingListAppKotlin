package com.example.shoppinglistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.shoppinglistapp.ui.theme.ShoppingListAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val locationViewModel: LocationViewModel = viewModel()
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)

    NavHost(navController = navController, startDestination = "shoppingList") {
        composable("shoppingList") {
            ShoppingListApp(
                navController = navController,
                viewModel = locationViewModel,
                locationUtils = locationUtils,
                context = context,
                address = locationViewModel.address.value.firstOrNull()?.formatted_address
                    ?: "No Address Found"
            )
        }
        dialog("locationSelection") {
            locationViewModel.locationData.value?.let { it1 ->
                LocationSelection(
                    onLocationSelected = {
                        locationViewModel.setAddress("${it.latitude},${it.longitude}")
                        navController.popBackStack()
                    },
                    locationData = it1
                )
            }
        }
    }
}