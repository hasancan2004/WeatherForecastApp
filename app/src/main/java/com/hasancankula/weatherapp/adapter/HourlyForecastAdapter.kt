package com.hasancankula.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hasancankula.weatherapp.R
import com.hasancankula.weatherapp.databinding.RowHourlyForecastBinding
import com.hasancankula.weatherapp.model.HourlyForecast

class HourlyForecastAdapter(private val hourlyForecastList: List<HourlyForecast>) : RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        val binding = RowHourlyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourlyForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        val hourlyForecast = hourlyForecastList[position]

        // Set data to your views
        holder.binding.tvHour.text = hourlyForecast.dateTimeText
        //val temperature = hourlyForecast.main?.temp?.minus(273.15)?.toInt() ?: Int.MIN_VALUE
        val temperature  = hourlyForecast.main?.temp?.let { (it).toInt()}
        holder.binding.tvTemperature.text = if(temperature != Int.MIN_VALUE) "$temperature°C"
        else "N/A"
    // Celcius Dönüşümü

        val weatherIconRes = getWeatherIcon(hourlyForecast.weather?.firstOrNull()?.icon)
        holder.binding.imgWeatherIcon.setImageResource(weatherIconRes)


    }

    override fun getItemCount(): Int {
        return hourlyForecastList.size
    }

    private fun getWeatherIcon(iconCode: String?): Int{
        return when(iconCode){
            "01d" -> R.drawable.bg_sunny
            "02d" -> R.drawable.bg_partly_cloudy
            "03d" -> R.drawable.bg_cloudy
            "04d" -> R.drawable.bg_overcast
            "09d" -> R.drawable.bg_rainy
            "10d" -> R.drawable.bg_showers
            "11d" -> R.drawable.bg_thunderstorm
            "13d" -> R.drawable.bg_snowy
            "50d" -> R.drawable.bg_mist
            else  -> R.drawable.blue_sky

        }
    }

    class HourlyForecastViewHolder(val binding: RowHourlyForecastBinding) : RecyclerView.ViewHolder(binding.root)
}
