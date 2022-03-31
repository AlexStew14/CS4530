package com.example.a4530project1

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
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

class WeatherActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var cityTV: TextView
    private lateinit var tempTV: TextView
    private lateinit var statusTV: TextView

    private lateinit var locationManager: LocationManager

    private lateinit var viewModel : DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        cityTV = findViewById(R.id.tv_city_name)
        tempTV = findViewById(R.id.tv_temperature)
        statusTV = findViewById(R.id.tv_weather_status)

        viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            findViewById<Button>(R.id.btn_hikes).setOnClickListener(this)
            findViewById<Button>(R.id.btn_fitness_goals).setOnClickListener(this)
            findViewById<Button>(R.id.btn_profile).setOnClickListener(this)
        }

        if (!this.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            val text = "This device does not have GPS, this feature will not work."
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        }
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
            getWeather()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getWeather()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getWeather() {
        viewModel.getWeatherData(locationManager, cityTV, tempTV, statusTV)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_hikes -> {
                val gmmIntentURI = Uri.parse("geo:0,0?q=hikes")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentURI)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            R.id.btn_fitness_goals -> {
                val intent = Intent(this@WeatherActivity, FitnessGoalsActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_profile -> {
                val intent = Intent(this@WeatherActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }



}