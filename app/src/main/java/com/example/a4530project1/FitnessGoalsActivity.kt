package com.example.a4530project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class FitnessGoalsActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitness_goals)

        findViewById<Button>(R.id.btn_BMI).setOnClickListener(this)
        findViewById<Button>(R.id.btn_fitness_goals_edit).setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_BMI -> {
                val intent = Intent(this@FitnessGoalsActivity, BMIActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_fitness_goals_edit -> {
                val intent = Intent(this@FitnessGoalsActivity, EditFitnessGoalsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}