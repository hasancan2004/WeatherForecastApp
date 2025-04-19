package com.hasancankula.weatherapp.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hasancankula.weatherapp.BuildConfig
import com.hasancankula.weatherapp.R
import com.hasancankula.weatherapp.adapter.HourlyForecastAdapter
import com.hasancankula.weatherapp.adapter.WeatherAdapter
import com.hasancankula.weatherapp.databinding.ActivityMainBinding


import com.hasancankula.weatherapp.model.HourlyForecast
import com.hasancankula.weatherapp.model.HourlyForecastResponse
import com.hasancankula.weatherapp.model.TempPoint

import com.hasancankula.weatherapp.model.WeatherResponse
import com.hasancankula.weatherapp.notification.NotificationHelper
import com.hasancankula.weatherapp.service.RetrofitClient
import com.hasancankula.weatherapp.service.WeatherRepository
import com.hasancankula.weatherapp.utils.PrefHelper

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private lateinit var hourlyForecastRecyclerView: RecyclerView
    private lateinit var hourlyForecastAdapter: HourlyForecastAdapter
    private val hourlyForecastList =
        mutableListOf<HourlyForecast>() // List to hold hourly forecast data
    private lateinit var binding: ActivityMainBinding

    // private val Base_URL = "https://api.openweathermap.org/data/2.5/"
    private lateinit var recyclerView: RecyclerView
    private val compositeDisposable = CompositeDisposable()
    private val weatherService = RetrofitClient.weatherService
    private val weatherList = mutableListOf<WeatherResponse>()
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var weatherRepository: WeatherRepository

    private val PERMISSION_REQUEST_CODE = 1001  // İzin istemek için belirlediğiniz kod

    val apiKey = BuildConfig.WEATHER_API_KEY


    // Konum İzin Kontrolü

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // İzin Verildi
                getLocationAndWeatherData()

            } else {
                // Konum Alınamadı Diyecez
                Toast.makeText(this, "Konum İzni Verilmedi!!!", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val selectedCity = intent.getStringExtra("selectedCity")
        if(!selectedCity.isNullOrEmpty()){
            getWeatherDataByCity(selectedCity)
            binding.cityEditText.setText(selectedCity)
        }

        weatherRepository = WeatherRepository(RetrofitClient.weatherService)


        // Bildirim izin kontrolü
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                // Bildirimleri gönderebilirsiniz
                sendWeatherNotification() // Bildirim gönderme fonksiyonu
            } else {
                // İzin isteme
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
        } else {
            // Android 13 ve sonrası için izin kontrolü yapılmaz, direkt bildirim gönderilebilir
            sendWeatherNotification() // Bildirim gönderme fonksiyonu
        }


// https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}

        supportActionBar?.apply {
            title = getString(R.string.app_name)
        } // Uygulama adı setDisplayShowTitleEnabled(true)  (mainActivity)
        // api = ec888c151772cd262d5423df2c129bc6


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupRecyclerView() // Hava durumu için set up recycler view setup ı
        setupHourlyForecastRecyclerView() // Saatlik hava durumu için set up recycler view setup ı


        checkLocationPermission()

        // Şehir Adı İle Hava Durumu Sorgulama
        binding.searchButton.setOnClickListener {
            val cityName = binding.cityEditText.text.toString()
            if (cityName.isNotEmpty()) {
                getWeatherDataByCity(cityName)

            } else {
                Toast.makeText(
                    this, "Lütfen geçerli bir şehir adı giriniz!!!", Toast
                        .LENGTH_SHORT
                ).show()

            }
        }

        binding.favoriteButton.setOnClickListener {
            val cityName = binding.cityEditText.text.toString()
            if(cityName.isNotEmpty()){
                PrefHelper.saveFavoriteCity(this, cityName)
                Toast.makeText(this, "$cityName favorilere eklendi!", Toast.LENGTH_SHORT).show()

            }
            else {
                Toast.makeText(this, "Lütfen önce bir şehir adı giriniz!!!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.openFavoritesButton.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }

    }



    // İzin talebine kullanıcı cevabı
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // İzin verildiyse, bildirim gönderilebilir
                sendWeatherNotification()
            } else {
                // İzin verilmediyse kullanıcıya bildirimde bulunabilirsiniz
                Toast.makeText(this, "Bildirim izni gerekli", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Bu fonksiyon MainActivity içinde olabilir.


    // Bildirim gönderme fonksiyonu
    private fun sendWeatherNotification() {
        // Burada bildirim gönderme kodunu yazabilirsiniz
        val notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannel()
        notificationHelper.sendWeatherNotification("Bugün hava güzel!")
    }

    private fun getHourlyForecastData(lat: Double, lon: Double) {
        val disposableHourly = weatherService.getHourlyForecast(lat, lon, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                // Successfully received hourly forecast data
                updateHourlyForecastList(response)
            }, { error ->
                // Handle error
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })
        compositeDisposable.add(disposableHourly)
    }
    private fun updateHourlyForecastList(response: HourlyForecastResponse) {
        hourlyForecastList.clear() // Clear previous data
        hourlyForecastList.addAll(response.forecastList ?: emptyList()) // Add new hourly forecast data
        hourlyForecastAdapter.notifyDataSetChanged() // Notify adapter to refresh the list


        val tempPoints = response.forecastList?.mapNotNull { forecast ->
            val temp = forecast.main?.temp
            val time = forecast.dateTimeText
            if (temp != null && time != null) {
                TempPoint(temp.toFloat(), time)
            } else null
        } ?: emptyList()
        binding.temperatureGraph.tempPoints = tempPoints

    }
    private fun setupHourlyForecastRecyclerView() {
        hourlyForecastRecyclerView = binding.hourlyForecastRecyclerView
        hourlyForecastRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hourlyForecastAdapter = HourlyForecastAdapter(hourlyForecastList)
        hourlyForecastRecyclerView.adapter = hourlyForecastAdapter
    }


    private fun setupRecyclerView(){
        recyclerView = binding.weatherRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        weatherAdapter = WeatherAdapter(weatherList)
        recyclerView.adapter = weatherAdapter
    }

    private fun checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED){
            getLocationAndWeatherData()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Şehir Adı ile hava durumu verisini al
    private fun getWeatherDataByCity(cityName: String) {

        val disposableCity = weatherService.getWeatherByCity(cityName, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                // Başarılı Yanıt
               updateWeatherList(response)
                val lat = response.coord.lat
                val lon = response.coord.lon
                getHourlyForecastData(lat, lon)
            }, { error ->
                // Hata Durumu
                Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show()
            })
        compositeDisposable.add(disposableCity)
    }

    // Konumu al ve hava durumu verisini çek
    private fun getLocationAndWeatherData(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED){
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
           location?.let {
               getWeatherData(it.latitude, it.longitude)
               getHourlyForecastData(it.latitude, it.longitude)

           } ?: run {
               Toast.makeText(this, "Konum Alınmadı!", Toast.LENGTH_SHORT).show()
           }
        }
    }

    // Hava Durumu Verisini API' den al
    private fun getWeatherData(lat: Double, lon: Double) {

        val disposableWeather = weatherService.getWeather(lat, lon, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                // Başarılı Yanıt
                updateWeatherList(response)

                    }, { error ->
                        // Hata Durumu
                        Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show()
                    })

        compositeDisposable.add(disposableWeather)
            }

    private fun updateWeatherList(response: WeatherResponse){
        weatherList.clear() // Önceki verileri temizle
        weatherList.add(response) // Yeni veri ekle
        weatherAdapter.notifyDataSetChanged() // Adapterı güncelle

        val weatherMain =  response.weather[0].main?.lowercase() ?: ""

        val animation = when {
            "rain" in weatherMain -> "rainy.json"
            "cloud" in weatherMain -> "cloudy.json"
            "clear" in weatherMain -> "sunny.json"
            else -> "sunny.json"
        }

        binding.animationView.setAnimation(animation)
        binding.animationView.visibility = View.VISIBLE
        binding.animationView.playAnimation()
        binding.animationView.background = null

// 3 saniye sonra animasyonu gizle
        Handler(Looper.getMainLooper()).postDelayed({
            binding.animationView.cancelAnimation() // animasyonu durdur
            binding.animationView.visibility = View.GONE // görünmez yap
        }, 3000) // 3 saniye
    }




    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear() // Activity destroy olduğunda tüm disposable' ları temizler
    }



}

