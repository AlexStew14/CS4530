package com.example.a4530project1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.runBlocking
import java.io.File


class MainActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var viewModel : DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(this.applicationContext)

            Log.i("MainActivity", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("MainActivity", "Could not initialize Amplify", error)
        }

        findViewById<Button>(R.id.btn_weather).setOnClickListener(this)
        findViewById<Button>(R.id.btn_hikes).setOnClickListener(this)
        findViewById<Button>(R.id.btn_fitness_goals).setOnClickListener(this)
        findViewById<Button>(R.id.btn_profile).setOnClickListener(this)
        findViewById<Button>(R.id.btn_step_counter).setOnClickListener(this)

        viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        val personalData: User? = viewModel.getPersonalData()

        if (personalData == null) {
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
        }
        else {
            if (personalData.profilePicture.isNotEmpty()) {
                findViewById<ImageView>(R.id.img_profile_picture).setImageURI(Uri.parse(personalData.profilePicture))
            }
        }

        var file = File(filesDir,"tracking.txt")
        if (!file.exists()) {
            File(filesDir,"tracking.txt").printWriter().use { out ->
                out.print(false)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val personalData: User? = viewModel.getPersonalData()

        if (personalData != null) {
            if (personalData.profilePicture.isNotEmpty()) {
                findViewById<ImageView>(R.id.img_profile_picture).setImageURI(Uri.parse(personalData.profilePicture))
            }
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
            R.id.btn_step_counter -> {
                val intent = Intent(this@MainActivity, StepCounterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}