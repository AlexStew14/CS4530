package com.example.a4530project1

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.Validators.not
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import kotlin.math.pow
import kotlin.math.roundToInt

class FitnessGoalsActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitness_goals)

        if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            findViewById<Button>(R.id.btn_weather).setOnClickListener(this)
            findViewById<Button>(R.id.btn_hikes).setOnClickListener(this)
            findViewById<Button>(R.id.btn_profile).setOnClickListener(this)
        }

        findViewById<Button>(R.id.btn_fitness_goals_edit).setOnClickListener(this)

        // TODO check if this is users first time opening fitness module,
        // if so prompt for initial data else display everything

        val userJSON = File(filesDir, "userData.txt").readText(Charsets.UTF_8)
        val mapper = jacksonObjectMapper()
        val user: User = mapper.readValue(userJSON)

        var weight = user.weight.toIntOrNull()
        var age = user.age.toIntOrNull()
        if (weight == null || age == null) {
            val text = "Must enter weight & age in profile to use feature"
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()

            finish()
        }
        else if (!File(filesDir,"fitnessGoalData.txt").exists()) {
            val intent = Intent(this@FitnessGoalsActivity, EditFitnessGoalsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // get file contents
        val user_file = File(filesDir, "userData.txt")
        if (user_file.exists()) {
            val userJSON = File(filesDir, "userData.txt").readText(Charsets.UTF_8)

            val mapper = jacksonObjectMapper()
            val user: User = mapper.readValue(userJSON)

            val weight = user.weight.toInt()
            val age = user.age.toInt()

            val heightInInches = (user.height[0].toString()
                .toDouble() * 12) + (user.height[2].toString().toDouble())
            findViewById<TextView>(R.id.tv_bmi).text = "BMI: " + ((weight / heightInInches.pow(2.0)) * 703).roundToInt().toString()

            val fg_file = File(filesDir,"fitnessGoalData.txt")

            if (fg_file.exists()) {
                val fitnessGoalJSON = fg_file.readText(Charsets.UTF_8)

                val mapper = jacksonObjectMapper()
                val fg: fitnessGoal = mapper.readValue(fitnessGoalJSON)

                findViewById<TextView>(R.id.tv_weight_goal).text = "Weight Goal: " + fg.weightGoal
                findViewById<TextView>(R.id.tv_activity_level).text = "Activity Level: " + fg.activityLevel
                findViewById<TextView>(R.id.tv_pounds_per_week).text = "Pounds Per Week: " + fg.poundsPerWeek.toString()

                var bmr = if (user.sex == "Male") {
                    66.47 + (6.24 * weight) + (12.7 * heightInInches) - (6.755 * age)
                } else {
                    655.1 + (4.35 * weight) + (4.7 * heightInInches) - (4.7 * age)
                }

                var calGoal = bmr
                if(fg.activityLevel == "Sedentary")
                    calGoal *= 1.2
                else
                    calGoal *= 1.6

                if (fg.weightGoal == "Lose Weight") {
                    calGoal -= 500 * fg.poundsPerWeek
                }
                else if (fg.weightGoal == "Gain Weight") {
                    calGoal += 500 * fg.poundsPerWeek
                }

                if (user.sex == "Male" && calGoal < 1200 ) {
                    findViewById<TextView>(R.id.tv_calories_needed).text = "Calorie Goal: " + calGoal.roundToInt().toString()
                    findViewById<TextView>(R.id.tv_calorie_warning).text = "WARNING: Too few calories"
                }
                else if (calGoal < 1000 ) {
                    findViewById<TextView>(R.id.tv_calories_needed).text = "Calorie Goal: " + calGoal.roundToInt().toString()
                    findViewById<TextView>(R.id.tv_calorie_warning).text = "WARNING: Too few calories"
                }
                else {
                    findViewById<TextView>(R.id.tv_calories_needed).text = "Calorie Goal: " + calGoal.roundToInt().toString()
                    findViewById<TextView>(R.id.tv_calorie_warning).text = ""
                }
            }
        }
    }



    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_fitness_goals_edit -> {
                val intent = Intent(this@FitnessGoalsActivity, EditFitnessGoalsActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_weather -> {
                val intent = Intent(this@FitnessGoalsActivity, WeatherActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_hikes -> {
                val gmmIntentURI = Uri.parse("geo:0,0?q=hikes")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentURI)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            R.id.btn_profile -> {
                val intent = Intent(this@FitnessGoalsActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }
}