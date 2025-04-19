package com.hasancankula.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hasancankula.weatherapp.R
import org.w3c.dom.Text

class FavoriteCityAdapter(
    private val cityList: List<String>,
    private val onItemClick : (String) -> Unit
) : RecyclerView.Adapter<FavoriteCityAdapter.CityViewHolder>(){
    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityText: TextView = itemView.findViewById(R.id.cityNameText)
        init {
            itemView.setOnClickListener {
                val city = cityList[adapterPosition]
                onItemClick(city)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_city, parent, false)
        return CityViewHolder(view)
    }

    override fun getItemCount(): Int = cityList.size

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.cityText.text = cityList[position]
    }

}
