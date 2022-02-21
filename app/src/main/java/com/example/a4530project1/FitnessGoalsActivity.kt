package com.example.a4530project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
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

        findViewById<Button>(R.id.btn_fitness_goals_edit).setOnClickListener(this)

        // TODO check if this is users first time opening fitness module,
        // if so prompt for initial data else display everything

        val fg_file = File(filesDir,"fitnessGoalData.txt")

        if (!fg_file.exists()) {
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

            val heightInInches = (user.height[0].toString()
                .toDouble() * 12) + (user.height[2].toString().toDouble())
            findViewById<TextView>(R.id.tv_bmi).text = ((user.weight / heightInInches.pow(2.0)) * 703).roundToInt().toString()

            val fg_file = File(filesDir,"fitnessGoalData.txt")

            if (fg_file.exists()) {
                val fitnessGoalJSON = fg_file.readText(Charsets.UTF_8)

                val mapper = jacksonObjectMapper()
                val fg: fitnessGoal = mapper.readValue(fitnessGoalJSON)

                findViewById<TextView>(R.id.tv_weight_goal).text = fg.weightGoal
                findViewById<TextView>(R.id.tv_activity_level).text = fg.activityLevel
                findViewById<TextView>(R.id.tv_pounds_per_week).text = fg.poundsPerWeek.toString()

                var bmr = if (user.sex == "Male") {
                    66.47 + (6.24 * user.weight) + (12.7 * heightInInches) - (6.755 * user.age)
                } else {
                    655.1 + (4.35 * user.weight) + (4.7 * heightInInches) - (4.7 * user.age)
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
                    findViewById<TextView>(R.id.tv_calories_needed).text = calGoal.roundToInt().toString() + " WARNING: Too few calories"
                }
                else if (calGoal < 1000 ) {
                    findViewById<TextView>(R.id.tv_calories_needed).text = calGoal.roundToInt().toString() + " WARNING: Too few calories"
                }
                else {
                    findViewById<TextView>(R.id.tv_calories_needed).text = calGoal.roundToInt().toString()
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
        }
    }
}