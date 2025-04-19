package com.hasancankula.weatherapp.utils

import android.content.Context

object PrefHelper {
    private const val PREF_NAME = "WeatherAppPrefs"
    private const val KEY_FAVORITES = "favorite_cities"

    fun saveFavoriteCity(context: Context, cityName: String) {
         val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
         val currentSet = prefs.getStringSet(KEY_FAVORITES, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        currentSet.add(cityName)
        prefs.edit().putStringSet(KEY_FAVORITES, currentSet).apply()
    }

    fun getFavoriteCities(context:Context): Set<String>{
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_FAVORITES, setOf()) ?: setOf()



    }
}