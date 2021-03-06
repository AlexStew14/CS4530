package com.example.a4530project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class EditFitnessGoalsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel : DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_fitness_goals)

        findViewById<Button>(R.id.btn_edit_fitness_goals_submit).setOnClickListener(this)

        viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        val fitnessData: FitnessGoal? = viewModel.getFitnessGoalData()

        if (fitnessData != null) {

            val wg_spinner: Spinner = findViewById(R.id.sp_weight_goal)
            ArrayAdapter.createFromResource(this,R.array.weight_goal_array,android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                wg_spinner.adapter = adapter
                wg_spinner.setSelection(adapter.getPosition(fitnessData.weightGoal))
            }
            val al_spinner: Spinner = findViewById(R.id.sp_activity_level)
            ArrayAdapter.createFromResource(this,R.array.activity_level_array,android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                al_spinner.adapter = adapter
                al_spinner.setSelection(adapter.getPosition(fitnessData.activityLevel))
            }
            val ppw_spinner: Spinner = findViewById(R.id.sp_pounds_per_week)
            ArrayAdapter.createFromResource(this,R.array.pounds_per_week_array,android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                ppw_spinner.adapter = adapter
                ppw_spinner.setSelection(adapter.getPosition(fitnessData.poundsPerWeek.toString()))
            }
        }
        else {
            val wg_spinner: Spinner = findViewById(R.id.sp_weight_goal)
            ArrayAdapter.createFromResource(this,R.array.weight_goal_array,android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                wg_spinner.adapter = adapter
                //wg_spinner.setSelection(adapter.getPosition(fitnessGoalFromJSON.weightGoal))
            }
            val al_spinner: Spinner = findViewById(R.id.sp_activity_level)
            ArrayAdapter.createFromResource(this,R.array.activity_level_array,android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                al_spinner.adapter = adapter
                //al_spinner.setSelection(adapter.getPosition(fitnessGoalFromJSON.activityLevel))
            }
            val ppw_spinner: Spinner = findViewById(R.id.sp_pounds_per_week)
            ArrayAdapter.createFromResource(this,R.array.pounds_per_week_array,android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                ppw_spinner.adapter = adapter
                //ppw_spinner.setSelection(adapter.getPosition(fitnessGoalFromJSON.poundsPerWeek.toString()))
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_edit_fitness_goals_submit -> {
                val weightGoal = findViewById<Spinner>(R.id.sp_weight_goal).selectedItem.toString()
                val activityLevel = findViewById<Spinner>(R.id.sp_activity_level).selectedItem.toString()
                val poundsPerWeek = findViewById<Spinner>(R.id.sp_pounds_per_week).selectedItem.toString().toFloat()
                val fitnessGoal = FitnessGoal(weightGoal=weightGoal, activityLevel=activityLevel, poundsPerWeek=poundsPerWeek)
                var file = File(this.filesDir,"fitnessKey.txt")
                if (file.exists()) {
                    viewModel.updateFitnessGoalData(fitnessGoal)
                }
                else {
                    viewModel.insertFitnessData(fitnessGoal)
                }

                finish()
            }
        }
    }
}