package com.example.a4530project1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.*
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.function.Consumer
import kotlin.math.roundToInt

class WeatherActivity : AppCompatActivity() {
    private val apiKey: String = "86645aa7651e68753af7d48496c69f36"

    private lateinit var locationManager: LocationManager
    private lateinit var  geocoder: Geocoder

    private lateinit var cityTV: TextView
    private lateinit var tempTV: TextView
    private lateinit var statusTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        cityTV = findViewById(R.id.tv_city_name)
        tempTV = findViewById(R.id.tv_temperature)
        statusTV = findViewById(R.id.tv_weather_status)

        geocoder = Geocoder(this, Locale.getDefault())
    }

    override fun onResume() {
        super.onResume()
        handleLocationPermissions()
    }

    private fun handleLocationPermissions() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        }
        else{
            getCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(){
        val locationCallback = Consumer<Location> {location ->
            if (location != null){
                val addresses: List<Address?> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (addresses.isNotEmpty() && addresses[0] != null){
                    cityTV.text = "City: " + addresses[0]!!.locality
                    getWeather(addresses[0]!!.latitude, addresses[0]!!.longitude)
                }
            }
        }

        locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null, this.mainExecutor, locationCallback)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()
            }
        }
    }

    private fun getWeather(latitude: Double, longitude: Double){
        GlobalScope.launch( Dispatchers.IO ) {
            val connection = URL("https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$apiKey&units=imperial").openConnection() as HttpURLConnection
            val data: String = connection.inputStream.bufferedReader().readText()
            withContext(Dispatchers.Main){
                val obj = JSONObject(data)
                val main = obj.getJSONObject("main")
                val temp = main.getDouble("temp")
                val weather = obj.getJSONArray("weather").getJSONObject(0)
                val status = weather.getString("description")
                tempTV.text = "Temp: " + temp.roundToInt() + "f"
                statusTV.text = "Weather Status: " + status
            }

            //Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
        }
    }



}