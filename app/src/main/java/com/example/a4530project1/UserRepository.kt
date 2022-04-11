package com.example.a4530project1

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.widget.TextView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.File
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import kotlin.math.roundToInt


class UserRepository(val context: Context) {
    private val apiKey: String = "86645aa7651e68753af7d48496c69f36"
    private lateinit var  geocoder: Geocoder
    private lateinit var locationManager: LocationManager

    private lateinit var cityTV: TextView
    private lateinit var tempTV: TextView
    private lateinit var statusTV: TextView

    private lateinit var userDAO : UserDatabaseDAO

    private val databaseExecutor: ExecutorService = Executors.newFixedThreadPool(4)

    init {
        var db = UserDatabase.Companion.getInstance(context)
        userDAO = db.userDatabaseDAO
    }

    suspend fun getUserData() : User? {
        var file = File(context.filesDir,"userKey.txt")
        if (file.exists()) {
            var res : User? = null
            runBlocking {
                res = userDAO.getUser(file.readText(Charsets.UTF_8).toLong())
            }
            return res
        }
        else {
            return null
        }
    }

    suspend fun updateUserData(data: User) {
        runBlocking {
            var key = File(context.filesDir,"userKey.txt").readText(Charsets.UTF_8).toLong()
            userDAO.insert(User(key, data.name, data.age, data.city, data.country, data.height, data.weight, data.sex, data.profilePicture))
        }
    }

    suspend fun insertUserData(data: User) {
        runBlocking {
            val key = userDAO.insert(data)
            File(context.filesDir,"userKey.txt").printWriter().use { out -> out.print(key) }
        }
    }

    suspend fun getFitnessData() : FitnessGoal? {
        var file = File(context.filesDir,"fitnessKey.txt")
        if (file.exists()) {
            var res : FitnessGoal? = null
            runBlocking {
                res = userDAO.getFitness(file.readText(Charsets.UTF_8).toLong())
            }
            return res
        }
        else {
            return null
        }
    }

    suspend fun insertFitnessData(data: FitnessGoal) {
        runBlocking {
            val key = userDAO.insert(data)
            File(context.filesDir,"fitnessKey.txt").printWriter().use { out -> out.print(key) }
        }
    }

    suspend fun updateFitnessData(data: FitnessGoal) {
        runBlocking {
            var key = File(context.filesDir,"fitnessKey.txt").readText(Charsets.UTF_8).toLong()
            userDAO.insert(FitnessGoal(key, data.weightGoal, data.activityLevel, data.poundsPerWeek))
        }
    }

    suspend fun getStepData() : StepData? {
        var file = File(context.filesDir,"stepKey.txt")
        if (file.exists()) {
            var res : StepData? = null
            runBlocking {
                res = userDAO.getStepData(file.readText(Charsets.UTF_8).toLong())
            }
            return res
        }
        else {
            return null
        }
    }

    suspend fun insertStepData(data: StepData) {
        runBlocking {
            val key = userDAO.insert(data)
            File(context.filesDir,"stepKey.txt").printWriter().use { out -> out.print(key) }
        }
    }

    suspend fun updateStepData(data: StepData) {
        runBlocking {
            var key = File(context.filesDir,"stepKey.txt").readText(Charsets.UTF_8).toLong()
            userDAO.insert(StepData(key, data.steps))
        }
    }

    @SuppressLint("SetTextI18n")
    suspend fun getWeatherData(locMag : LocationManager, cityTV : TextView, tempTV : TextView, statusTV : TextView){
        geocoder = Geocoder(context, Locale.getDefault())
        locationManager = locMag
        this.cityTV = cityTV
        this.tempTV = tempTV
        this.statusTV = statusTV

        try {
            getCurrentLocation()
        }
        catch (e: Exception) { // should run when we dont have internet access
            runBlocking {
                var key = File(context.filesDir,"weatherKey.txt").readText(Charsets.UTF_8).toLong()
                var weatherData = userDAO.getWeather(key)
                if (weatherData != null) {
                    tempTV.text = "Temp: " + weatherData.temp + "\u2109"
                    statusTV.text = "Weather Status: ${weatherData.status}"
                    cityTV.text = "City: " + weatherData.city
                }
            }
        }
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
                runBlocking { // cache weather
                    var key = userDAO.insert(WeatherData(temp=temp.roundToInt(), status=status, city=obj.getString("name")))
                    File(context.filesDir,"weatherKey.txt").printWriter().use { out -> out.print(key) }
                    userDAO.insert(WeatherData(key, temp.roundToInt(), status, obj.getString("name")))
                }
            }
        }

    }



}