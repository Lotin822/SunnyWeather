package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

/**
@author LT
@create 2022-05-26-15:25
 */
data class RealtimeResponse(val status: String, val result: Result) {
    data class Result(val realtime: Realtime)

    data class Realtime(val skycon: String, val temperature: Float, @SerializedName("air_quality") val airQuality: AirQuality)

    data class AirQuality(val aqi: AQI)

    data class AQI(val chn: Float)
}