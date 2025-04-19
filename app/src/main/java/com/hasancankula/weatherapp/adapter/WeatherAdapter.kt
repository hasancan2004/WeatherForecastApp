package com.hasancankula.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hasancankula.weatherapp.R
import com.hasancankula.weatherapp.model.WeatherResponse
import com.squareup.picasso.Picasso

class WeatherAdapter(private val weatherList: MutableList<WeatherResponse>) : RecyclerView
    .Adapter<WeatherAdapter.WeatherViewHolder>(){
    class WeatherViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView){
        val weatherDescriptionTextView: TextView = itemView.findViewById(R.id.weather_descriptionTextView)
        val temperatureTextView: TextView = itemView.findViewById(R.id.temperatureTextView)
        val weatherIcon: android.widget.ImageView = itemView.findViewById(R.id.weather_icon)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_weather, parent,false)
        return WeatherViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentWeather = weatherList[position]

        // Hava Durumu açıklaması ve sıcaklık
        val weather = currentWeather.weather.firstOrNull()
        val temperature = currentWeather.main.temp?.minus(273.15)?: 0.0
        // Kelvinden
        // Celciusa Dönüşümü


        holder.temperatureTextView.text = "${temperature.toInt()}°C"

        if(weather != null){
            holder.weatherDescriptionTextView.text = weather.description
            // Hava Durumu İkonu

            // Hava Durumu İkonu (API'den çekme işlemi)
            val iconUrl = "https://openweathermap.org/img/w/${weather.icon}.png"
            Picasso.get().load(iconUrl).into(holder.weatherIcon)
        } else{
            holder.weatherDescriptionTextView.text = "Bilinmiyor"
        }





    }

    override fun getItemCount(): Int {
        return weatherList.size

    }

}



