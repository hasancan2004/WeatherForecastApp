package com.hasancankula.weatherapp.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hasancankula.weatherapp.R

class NotificationHelper(private val context:Context) {
    private val CHANNEL_ID = "weather_channel_id"

    // Kanalı oluşturma fonksiyonu
    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val name = "Weather Notifications"
            val descriptionText = "Daily Weather Updates"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    // Hava durumu bildirimini gönderme fonksiyonu
    fun sendWeatherNotification(message: String){
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_weather) // Burayı düzeltmeliyiz
            .setContentTitle("Günlük Hava Durumu")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        // Bildirimi gönder
        notificationManager.notify(0, notificationBuilder.build())


    }

}