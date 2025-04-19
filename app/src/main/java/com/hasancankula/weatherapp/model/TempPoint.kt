package com.hasancankula.weatherapp.model

import android.health.connect.datatypes.units.Temperature
import java.sql.Timestamp

data class TempPoint(
    val temperature: Float,
    val timestamp: String
)