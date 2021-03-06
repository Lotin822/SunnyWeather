package com.sunnyweather.android.ui.weather

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sunnyweather.android.R
import com.sunnyweather.android.databinding.*
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

//    private lateinit var nowBinding: NowBinding
//    private lateinit var nowView: View
//    private lateinit var forecastBinding: ForecastBinding
//    private lateinit var forecastView: View
//    private lateinit var forecastItemBinding: ForecastItemBinding
//    private lateinit var forecastItemView: View
//    private lateinit var lifeIndexBinding: LifeIndexBinding
//    private lateinit var lifeIndexView: View
    private lateinit var activityWeatherBinding: ActivityWeatherBinding
//    private lateinit var activityWeatherView: View

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        activityWeatherBinding = ActivityWeatherBinding.inflate(layoutInflater)
//        activityWeatherView = activityWeatherBinding.root
        setContentView(activityWeatherBinding.root)
//        nowBinding = NowBinding.inflate(layoutInflater)
//        nowView = nowBinding.root
//        forecastBinding = ForecastBinding.inflate(layoutInflater)
//        forecastView = forecastBinding.root
//        forecastItemBinding = ForecastItemBinding.inflate(layoutInflater)
//        forecastItemView = forecastItemBinding.root
//        lifeIndexBinding = LifeIndexBinding.inflate(layoutInflater)
//        lifeIndexView = lifeIndexBinding.root
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "??????????????????????????????", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            activityWeatherBinding.swipeRefresh.isRefreshing = false
        })
        activityWeatherBinding.swipeRefresh.setColorSchemeResources(R.color.design_default_color_primary)
        refreshWeather()
        activityWeatherBinding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        activityWeatherBinding.nowLayout.navBtn.setOnClickListener {
            activityWeatherBinding.drawerLayout.openDrawer(GravityCompat.START)
        }
        activityWeatherBinding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onDrawerStateChanged(newState: Int) {}

        })
    }

    private fun showWeatherInfo(weather: Weather) {
//        nowView.findViewById<TextView>(R.id.placeName).text = viewModel.placeName
        activityWeatherBinding.nowLayout.placeName.text = viewModel.placeName
        val realtime  =weather.realtime
        val daily = weather.daily
        //??????now.xml??????????????????
        val currentTempText = "${realtime.temperature.toInt()} ???"
//        nowView.findViewById<TextView>(R.id.currentTemp).text = currentTempText
        activityWeatherBinding.nowLayout.currentTemp.text = currentTempText
//        nowView.findViewById<TextView>(R.id.currentSky).text = getSky(realtime.skycon).info
        activityWeatherBinding.nowLayout.currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "???????????? ${realtime.airQuality.aqi.chn.toInt()}"
//        nowView.findViewById<TextView>(R.id.currentAQI).text = currentPM25Text
        activityWeatherBinding.nowLayout.currentAQI.text = currentPM25Text
//        nowView.findViewById<RelativeLayout>(R.id.nowLayout).setBackgroundResource(getSky(realtime.skycon).bg)
        activityWeatherBinding.nowLayout.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        //??????forecast.xml??????????????????
        val forecastLayout = activityWeatherBinding.forecastLayout.forecastLayout
        forecastLayout.removeAllViews()
//        forecastBinding.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)
            val dateInfo = view.findViewById<TextView>(R.id.dateInfo)
            val skyIcon = view.findViewById<ImageView>(R.id.skyIcon)
            val skyInfo = view.findViewById<TextView>(R.id.skyInfo)
            val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
//            forecastItemBinding.dataInfo.text = simpleDateFormat.format(skycon.data)
//            val sky = getSky(skycon.value)
//            forecastItemBinding.skyIcon.setImageResource(sky.icon)
//            forecastItemBinding.skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ???"
//            forecastItemBinding.temperatureInfo.text = tempText
//            forecastBinding.forecastLayout.addView(forecastItemBinding.root)
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }
        //??????life_index.xml??????????????????
        val lifeIndex = daily.lifeIndex
//        lifeIndexView.findViewById<TextView>(R.id.coldRiskText).text = lifeIndex.coldRisk[0].desc
        activityWeatherBinding.lifeIndexLayout.coldRiskText.text = lifeIndex.coldRisk[0].desc
//        lifeIndexView.findViewById<TextView>(R.id.dressingText).text = lifeIndex.dressing[0].desc
        activityWeatherBinding.lifeIndexLayout.dressingText.text = lifeIndex.dressing[0].desc
//        lifeIndexView.findViewById<TextView>(R.id.ultravioletText).text = lifeIndex.ultraviolet[0].desc
        activityWeatherBinding.lifeIndexLayout.ultravioletText.text = lifeIndex.ultraviolet[0].desc
//        lifeIndexView.findViewById<TextView>(R.id.carWashingText).text = lifeIndex.carWashing[0].desc
        activityWeatherBinding.lifeIndexLayout.carWashingText.text = lifeIndex.carWashing[0].desc
//        activityWeatherView.findViewById<ScrollView>(R.id.weatherLayout).visibility = View.VISIBLE
        activityWeatherBinding.weatherLayout.visibility = View.VISIBLE
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        activityWeatherBinding.swipeRefresh.isRefreshing = true
    }
}