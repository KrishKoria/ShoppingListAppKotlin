package com.example.shoppinglistapp

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel(){
    private var _locationData = mutableStateOf<LocationData?>(null)
    val locationData: State<LocationData?> = _locationData

    private var _address = mutableStateOf(listOf<GeocodingResult>())
    val address: State<List<GeocodingResult>> = _address
    fun setLocationData(locationData: LocationData){
        _locationData.value = locationData
    }
    fun setAddress(latLng: String){
        try {
            viewModelScope.launch {
                val response = RetrofitClient.create().getGeocodingData(latLng, "AIzaSyBvb2NIziYCblvCzUXau903z84wRON7Jxw")
                _address.value = response.results
            }
        } catch (e: Exception) {
            Log.d("res1","${e.cause} ${e.message}")
        }
    }
}