package com.hasancankula.weatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val coord : Coord,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind?,
    val rain: Rain?,
    val clouds: Clouds?,
    val sys: Sys?,
    val name: String


)
data class Coord(
    val lon: Double,
    val lat: Double

)
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String

)
data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)
data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double? // Nullable
)
data class Rain(
    @SerializedName("1h")

    val oneHour: Double?

)
data class Clouds(
    val all: Int?
)
data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String?,
    val sunrise: Long?,
    val sunset: Long?
)




