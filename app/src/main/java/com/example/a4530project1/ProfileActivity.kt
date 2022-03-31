package com.example.a4530project1

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel : DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            findViewById<Button>(R.id.btn_weather).setOnClickListener(this)
            findViewById<Button>(R.id.btn_hikes).setOnClickListener(this)
            findViewById<Button>(R.id.btn_fitness_goals).setOnClickListener(this)
        }

        findViewById<Button>(R.id.btn_profile_edit).setOnClickListener(this)

        viewModel = ViewModelProvider(this).get(DataViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        val personalData: User? = viewModel.getPersonalData()

        if (personalData != null) {
            findViewById<TextView>(R.id.tv_name).text = "Name: " + personalData.name
            findViewById<TextView>(R.id.tv_age).text = "Age: " + personalData.age.toString()
            findViewById<TextView>(R.id.tv_city).text = "City: " + personalData.city
            findViewById<TextView>(R.id.tv_country).text = "Country: " + personalData.country
            findViewById<TextView>(R.id.tv_height).text = "Height: " + personalData.height
            findViewById<TextView>(R.id.tv_weight).text =
                "Weight: " + personalData.weight.toString()
            findViewById<TextView>(R.id.tv_sex).text = "Sex: " + personalData.sex
            if (personalData.profilePicture.isNotEmpty()) {
                findViewById<ImageView>(R.id.img_profile_picture).setImageURI(Uri.parse(personalData.profilePicture))
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