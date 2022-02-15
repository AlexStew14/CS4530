package com.example.a4530project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.lang.Math.pow
import java.lang.Math.round
import kotlin.math.pow
import kotlin.math.roundToInt

class FitnessGoalsActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitness_goals)

        findViewById<Button>(R.id.btn_fitness_goals_edit).setOnClickListener(this)

        // TODO check if this is users first time opening fitness module,
        // if so prompt for initial data else display everything

        // get file contents
        // TODO check if file exists
        val userJSON = File(filesDir,"userData.txt").readText(Charsets.UTF_8)

        val mapper = jacksonObjectMapper()
        val userFromJSON: User = mapper.readValue(userJSON)

        val heightInInches = (userFromJSON.height[0].toString().toDouble() * 12) + (userFromJSON.height[2].toString().toDouble())
        findViewById<TextView>(R.id.tv_bmi).text = "BMI: " + ((userFromJSON.weight / heightInInches.pow(2.0)) * 703).roundToInt().toString()
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