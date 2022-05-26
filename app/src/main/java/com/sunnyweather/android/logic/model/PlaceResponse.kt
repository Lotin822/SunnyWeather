package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

/**
@author LT
@create 2022-05-26-13:43
 */
data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(val name: String, val location: Location, @SerializedName("formatted_address") val address: String)

data class Location(val lng: String, val lat: String)