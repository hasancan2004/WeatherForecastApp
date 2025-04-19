package com.hasancankula.weatherapp.service

import com.hasancankula.weatherapp.BuildConfig

import com.hasancankula.weatherapp.model.GeoLocationResponse

import com.hasancankula.weatherapp.model.HourlyForecastResponse
import com.hasancankula.weatherapp.model.WeatherResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    // https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
    // https://pro.openweathermap.org/data/2.5/forecast/hourly?lat={lat}&lon={lon}&appid={API key}
    // Anlık hava durumu
    @GET("data/2.5/weather")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = BuildConfig.WEATHER_API_KEY,
        @Query("units") units: String = "metric" // Sıcaklık birimini cinsinden almak için 'metric'
    ): Single<WeatherResponse>

    // Şehir adına göre hava durumu
    @GET("data/2.5/weather")
    fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = BuildConfig.WEATHER_API_KEY,
        @Query("units") units: String = "metric"
    ): Single<WeatherResponse>

    // Saatlik hava durumu tahmini
    @GET("data/2.5/forecast")
    fun getHourlyForecast(
        @Query("lat") lat : Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = BuildConfig.WEATHER_API_KEY,
        @Query("units") units: String = "metric"
    ): Single<HourlyForecastResponse>

}

