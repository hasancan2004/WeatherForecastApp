package com.hasancankula.weatherapp.model


import com.google.gson.annotations.SerializedName

data class HourlyForecastResponse(
    @SerializedName("cod") val cod: String?,
    @SerializedName("message") val message: Int?,
    @SerializedName("cnt") val count: Int?,
    @SerializedName("list") val forecastList: List<HourlyForecast>?,
    @SerializedName("city") val city: City?
)
data class HourlyForecast(
    @SerializedName("dt") val timeStamp: Long?,
    @SerializedName("main") val main: ForecastMain?,
    @SerializedName("weather") val weather : List<ForecastWeather>?,
    @SerializedName("clouds") val clouds : ForecastClouds?,
    @SerializedName("wind") val wind: ForecastWind?,
    @SerializedName("visibility") val visibility: Int?,
    @SerializedName("pop") val pop: Double?,
    @SerializedName("rain") val rain: ForecastRain?,
    @SerializedName("sys") val sys: ForecastSys?,
    @SerializedName("dt_txt") val dateTimeText: String?
)

data class ForecastMain(
    @SerializedName("temp") val temp: Double?,
    @SerializedName("feels_like") val feelsLike: Double?,
    @SerializedName("temp_min") val tempMin: Double?,
    @SerializedName("temp_max") val tempMax : Double?,
    @SerializedName("pressure") val pressure: Int?,
    @SerializedName("humidity") val humidity:Int?
)

data class ForecastWeather(
    @SerializedName("id") val id: Int?,
    @SerializedName("main") val main: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("icon") val icon: String?
)
data class ForecastClouds(
    @SerializedName("all") val all: Int?
)

data class ForecastWind(
    @SerializedName("speed") val speed: Double?,
    @SerializedName("deg") val deg: Int?,
    @SerializedName("gust") val gust: Double? // Bazen olmayabilir
)

data class ForecastRain(
    @SerializedName("1h") val lastHour: Double? // JSON'da "rain": { "1h": 0.2 } şeklinde geldiği için
)

data class ForecastSys(
    @SerializedName("pod") val pod: String? // "d" -> gündüz, "n" -> gece
)

data class City(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("coord") val coord: Coord?,
    @SerializedName("country") val country: String?,
    @SerializedName("population") val population: Int?,
    @SerializedName("timezone") val timezone: Int?,
    @SerializedName("sunrise") val sunrise: Long?,
    @SerializedName("sunset") val sunset: Long?
)

