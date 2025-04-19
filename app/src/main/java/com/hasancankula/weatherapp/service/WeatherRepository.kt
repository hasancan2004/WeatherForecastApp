package com.hasancankula.weatherapp.service


import com.hasancankula.weatherapp.model.GeoLocationResponse
import com.hasancankula.weatherapp.model.HourlyForecastResponse
import com.hasancankula.weatherapp.model.WeatherResponse
import io.reactivex.rxjava3.core.Single

class WeatherRepository(private val weatherAPI: WeatherAPI) {

    // Şehir adına göre anlık hava durumu
    fun getWeatherByCity(cityName: String): Single<WeatherResponse> {
        return weatherAPI.getWeatherByCity(cityName)

    }

    // Şehir adına göre saatlik hava durumu
    fun getHourlyForecastByCity(lat: Double, lon : Double): Single<HourlyForecastResponse> {
        return weatherAPI.getHourlyForecast(lat, lon)
    }




}