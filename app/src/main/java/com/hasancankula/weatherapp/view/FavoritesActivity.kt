package com.hasancankula.weatherapp.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasancankula.weatherapp.R
import com.hasancankula.weatherapp.adapter.FavoriteCityAdapter
import com.hasancankula.weatherapp.databinding.ActivityFavoritesBinding
import com.hasancankula.weatherapp.utils.PrefHelper

class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val favoriteCities = PrefHelper.getFavoriteCities(this).toList().ifEmpty {
            listOf("Henüz favori şehir yok!!!")
        }

        val adapter = FavoriteCityAdapter(favoriteCities) { selectedCity ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("selectedCity", selectedCity)
            startActivity(intent)
        }

        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.favoritesRecyclerView.adapter = adapter

        binding.backButton.setOnClickListener {
            finish()
        }


    }
}