package com.example.shoppinglistapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel(){
    private var _locationData = mutableStateOf<LocationData?>(null)
    val locationData: LocationData? by _locationData

    fun setLocationData(locationData: LocationData){
        _locationData.value = locationData
    }
}