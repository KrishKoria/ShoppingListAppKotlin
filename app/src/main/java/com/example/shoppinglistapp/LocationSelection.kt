package com.example.shoppinglistapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationSelection(
    onLocationSelected: (LocationData) -> Unit,
    locationData: LocationData
) {
    var userLocationData by remember {
        mutableStateOf(LatLng(locationData.latitude, locationData.longitude))
    }
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocationData, 10f)
    }
    var newLocationData: LocationData

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp),
            cameraPositionState = cameraPosition,
            onMapClick = {
                userLocationData = it
            }) {
            Marker(state = MarkerState(position = userLocationData))
        }

        Button(onClick = {
            newLocationData = LocationData(
                latitude = userLocationData.latitude,
                longitude = userLocationData.longitude
            )
            onLocationSelected(newLocationData)
        }) {
            Text(text = "Set Location")
        }
    }
}