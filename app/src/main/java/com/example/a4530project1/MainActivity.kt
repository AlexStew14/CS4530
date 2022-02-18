package com.example.a4530project1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import java.io.File


class MainActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_weather).setOnClickListener(this)
        findViewById<Button>(R.id.btn_hikes).setOnClickListener(this)
        findViewById<Button>(R.id.btn_fitness_goals).setOnClickListener(this)
        findViewById<Button>(R.id.btn_profile).setOnClickListener(this)


        val file = File(filesDir,"userData.txt")

        if (!file.exists()) {
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_weather -> {
                val intent = Intent(this@MainActivity, WeatherActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_hikes -> {
                val gmmIntentURI = Uri.parse("geo:0,0?q=hikes")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentURI)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            R.id.btn_fitness_goals -> {
                val intent = Intent(this@MainActivity, FitnessGoalsActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_profile -> {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }
}