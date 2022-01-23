package com.example.a4530project1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.location.*
import androidx.core.app.ActivityCompat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener, LocationListener {
    private lateinit var submitButton: Button
    private lateinit var locationButton: Button
    private lateinit var locationManager: LocationManager

    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        submitButton = findViewById(R.id.bt_submit)
        submitButton.setOnClickListener(this)

        locationButton = findViewById(R.id.bt_location)
        locationButton.setOnClickListener(this)

        geocoder = Geocoder(this, Locale.getDefault())
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.bt_submit -> Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            R.id.bt_location -> getLocation()
        }
    }

    private fun getLocation(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLocationChanged(loc: Location) {
        val addresses: List<Address> = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
        if (addresses.isNotEmpty()){
            Toast.makeText(this, "city: " + addresses[0].locality, Toast.LENGTH_SHORT).show()
        }

    }
}