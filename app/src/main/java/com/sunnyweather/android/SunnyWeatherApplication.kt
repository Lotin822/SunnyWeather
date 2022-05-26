package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
@author LT
@create 2022-05-26-13:38
 */
class SunnyWeatherApplication: Application() {

    companion object {
        const val TOKEN = "IWhd102Pc3sUKI4m"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}