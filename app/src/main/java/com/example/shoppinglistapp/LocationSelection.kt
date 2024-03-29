package com.example.shoppinglistapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationSelection(
    onLocationSelected: (LocationData) -> Unit,
    locationData: LocationData
) {
    val userLocationData by remember {
        mutableStateOf(LatLng(locationData.latitude, locationData.longitude ))
    }
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocationData, 10f)
    }
}