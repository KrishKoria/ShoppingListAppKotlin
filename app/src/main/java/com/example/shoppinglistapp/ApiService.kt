package com.example.shoppinglistapp

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("maps/api/geocode/json")
    suspend fun getGeocodingData(
        @Query("latlng") latlng: String,
        @Query("key") apikey: String
    ): GeocodingResponse
}