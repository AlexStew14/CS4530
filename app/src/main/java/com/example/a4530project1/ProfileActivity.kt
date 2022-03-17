package com.example.a4530project1

import android.content.Intent
import android.content.res.Configuration
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            findViewById<Button>(R.id.btn_weather).setOnClickListener(this)
            findViewById<Button>(R.id.btn_hikes).setOnClickListener(this)
            findViewById<Button>(R.id.btn_fitness_goals).setOnClickListener(this)
        }

        findViewById<Button>(R.id.btn_profile_edit).setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()

        //TODO check if file exists
        val file = File(filesDir,"userData.txt")

        if (file.exists()) {
            val userJSON = file.readText(Charsets.UTF_8)

            val mapper = jacksonObjectMapper()
            val userFromJSON: User = mapper.readValue(userJSON)

            findViewById<TextView>(R.id.tv_name).text = "Name: " + userFromJSON.name
            findViewById<TextView>(R.id.tv_age).text = "Age: " + userFromJSON.age.toString()
            findViewById<TextView>(R.id.tv_city).text = "City: " + userFromJSON.city
            findViewById<TextView>(R.id.tv_country).text = "Country: " + userFromJSON.country
            findViewById<TextView>(R.id.tv_height).text = "Height: " + userFromJSON.height
            findViewById<TextView>(R.id.tv_weight).text = "Weight: " + userFromJSON.weight.toString()
            findViewById<TextView>(R.id.tv_sex).text = "Sex: " + userFromJSON.sex
            if (userFromJSON.profilePicture.isNotEmpty()) {
                findViewById<ImageView>(R.id.img_profile_picture).setImageURI(Uri.parse(userFromJSON.profilePicture))
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_profile_edit -> {
                val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_weather -> {
                val intent = Intent(this@ProfileActivity, WeatherActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_hikes -> {
                val gmmIntentURI = Uri.parse("geo:0,0?q=hikes")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentURI)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            R.id.btn_fitness_goals -> {
                val intent = Intent(this@ProfileActivity, FitnessGoalsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}