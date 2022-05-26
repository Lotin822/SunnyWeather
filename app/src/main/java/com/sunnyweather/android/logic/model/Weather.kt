package com.sunnyweather.android.logic.model

/**
@author LT
@create 2022-05-26-15:35
 */
data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily)
