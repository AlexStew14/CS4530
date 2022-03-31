package com.example.a4530project1

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.Executor
import java.util.function.Consumer
import kotlin.math.roundToInt

data class User (
    val name: String,
    val age: String,
    val city: String,
    val country: String,
    val height: String,
    val weight: String,
    val sex: String,
    val profilePicture: String
)

data class FitnessGoal  (
    val weightGoal: String,
    val activityLevel: String,
    val poundsPerWeek: Float
)

class UserRepository(val context: Context) {
    private val apiKey: String = "86645aa7651e68753af7d48496c69f36"
    private lateinit var  geocoder: Geocoder
    private lateinit var locationManager: LocationManager

    private lateinit var cityTV: TextView
    private lateinit var tempTV: TextView
    private lateinit var statusTV: TextView

    suspend fun getUserData() : User? {
        var file = File(context.filesDir,"userData.txt")
        if (file.exists()) {
            val userJSON = file.readText(Charsets.UTF_8)
            val mapper = jacksonObjectMapper()
            return mapper.readValue(userJSON)
        }
        else {
            return null
        }
    }

    suspend fun updateUserData(data: User) {
        val mapper = jacksonObjectMapper()
        val userJson = mapper.writeValueAsString(data)
        File(context.filesDir,"userData.txt").printWriter().use { out -> out.println(userJson) }
    }

    suspend fun getFitnessData() : FitnessGoal? {
        var file = File(context.filesDir,"fitnessGoalData.txt")
        if (file.exists()) {
            val fitnessJSON = file.readText(Charsets.UTF_8)
            val mapper = jacksonObjectMapper()
            return mapper.readValue(fitnessJSON)
        }
        else {
            return null
        }
    }

    suspend fun updateFitnessData(data: FitnessGoal) {
        val mapper = jacksonObjectMapper()
        val userJson = mapper.writeValueAsString(data)
        File(context.filesDir,"fitnessGoalData.txt").printWriter().use { out -> out.println(userJson) }
    }

    suspend fun getWeatherData(locMag : LocationManager, cityTV : TextView, tempTV : TextView, statusTV : TextView){
        geocoder = Geocoder(context, Locale.getDefault())
        locationManager = locMag
        this.cityTV = cityTV
        this.tempTV = tempTV
        this.statusTV = statusTV

        getCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val locationCallback = Consumer<Location> { location ->
            if (location != null){
                val addresses: List<Address?> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (addresses.isNotEmpty() && addresses[0] != null){
                    getWeather(addresses[0]!!.latitude, addresses[0]!!.longitude)
                }
            }
        }
        locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null, context.mainExecutor, locationCallback)

    }

    @SuppressLint("SetTextI18n")
    private fun getWeather(latitude: Double, longitude: Double) {
        GlobalScope.launch( Dispatchers.IO ) {
            val connection = URL("https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$apiKey&units=imperial").openConnection() as HttpURLConnection
            val data: String = connection.inputStream.bufferedReader().readText()

            withContext(Dispatchers.Main){
                val obj = JSONObject(data)
                val main = obj.getJSONObject("main")
                val temp = main.getDouble("temp")
                val weather = obj.getJSONArray("weather").getJSONObject(0)
                val status = weather.getString("description")
                tempTV.text = "Temp: " + temp.roundToInt() + "\u2109"
                statusTV.text = "Weather Status: $status"
                cityTV.text = "City: " + obj.getString("name")
            }
        }

    }



}