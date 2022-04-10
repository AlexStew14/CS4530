package com.example.a4530project1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import java.io.File

class StepCounterActivity : AppCompatActivity(), View.OnClickListener {

    private var tracking : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)
        findViewById<Button>(R.id.btn_step_counter_onoff).setOnClickListener(this)

        tracking = File(filesDir,"tracking.txt").readText(Charsets.UTF_8).toBoolean()

        if (tracking) {
            findViewById<Button>(R.id.btn_step_counter_onoff).text = "STOP TRACKING"
        }
        else {
            findViewById<Button>(R.id.btn_step_counter_onoff).text = "START TRACKING"
        }

    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_step_counter_onoff -> {
                tracking = !tracking
                File(filesDir,"tracking.txt").printWriter().use { out ->
                    out.print(tracking)
                }
                if (tracking) {
                    findViewById<Button>(R.id.btn_step_counter_onoff).text = "STOP TRACKING"
                }
                else {
                    findViewById<Button>(R.id.btn_step_counter_onoff).text = "START TRACKING"
                }
            }
        }
    }
}