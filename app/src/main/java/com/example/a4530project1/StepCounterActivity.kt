package com.example.a4530project1

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.ViewModelProvider
import java.io.File


class StepCounterActivity : AppCompatActivity(), View.OnClickListener {

    private var tracking : Boolean = false
    private lateinit var viewModel : DataViewModel
    private var steps : Int = 0
    private var lastClickTime: Long = 0

    private var mSensorManager : SensorManager? = null
    private var mStepCounter : Sensor? = null
    private var mLinearAccelerometer : Sensor? = null

    private val mThreshold = 2.0
    private var last_x : Float = 0.0f
    private var last_y : Float = 0.0f
    private var last_z : Float = 0.0f
    private var now_x : Float = 0.0f
    private var now_y : Float = 0.0f
    private var now_z : Float = 0.0f
    private var mNotFirstTime : Boolean = false


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)
        findViewById<RelativeLayout>(R.id.layout_step_counter).setOnClickListener(this)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mStepCounter = mSensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        mLinearAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        tracking = File(filesDir,"tracking.txt").readText(Charsets.UTF_8).toBoolean()

        if (tracking) {
            findViewById<TextView>(R.id.tv_tracking_status).text = "TRACKING: ON"
            findViewById<TextView>(R.id.tv_step_number).setTextColor(Color.parseColor("#008000"))
        }
        else {
            findViewById<TextView>(R.id.tv_step_number).setTextColor(Color.parseColor("#656565"))
            findViewById<TextView>(R.id.tv_tracking_status).text = "TRACKING: OFF"
        }

        val stepData: StepData? = viewModel.getStepData()

        if (stepData != null) {
            if (stepData.steps != null) {
                findViewById<TextView>(R.id.tv_prev_steps).text = "Last Tracked Steps: " + stepData.steps
            }
        }

    }

    private val mListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(sensorEvent: SensorEvent) {
            steps = sensorEvent.values[0].toInt()
            findViewById<TextView>(R.id.tv_step_number).text = steps.toString()
        }
        override fun onAccuracyChanged(sensor: Sensor, i: Int) {}
    }

    private val shakeListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(sensorEvent: SensorEvent) {
            //Get the acceleration rates along the y and z axes
            //Get the acceleration rates along the y and z axes
            now_x = sensorEvent.values[0]
            now_y = sensorEvent.values[1]
            now_z = sensorEvent.values[2]

            if (mNotFirstTime) {
                val dx = Math.abs(last_x - now_x).toDouble()
                val dy = Math.abs(last_y - now_y).toDouble()
                val dz = Math.abs(last_z - now_z).toDouble()

                //Check if the values of acceleration have changed on any pair of axes
                if (!tracking && (dx > mThreshold && dy > mThreshold || dx > mThreshold && dz > mThreshold || dy > mThreshold && dz > mThreshold)) {
                    // START TRACKING
                    findViewById<TextView>(R.id.tv_tracking_status).text = "TRACKING: ON"
                    findViewById<TextView>(R.id.tv_step_number).setTextColor(Color.parseColor("#008000"))
                    if (mStepCounter != null) {
                        mSensorManager?.registerListener(mListener,mStepCounter,SensorManager.SENSOR_DELAY_NORMAL)
                    }
                    tracking = true
                }
            }
            last_x = now_x
            last_y = now_y
            last_z = now_z
            mNotFirstTime = true
        }
        override fun onAccuracyChanged(sensor: Sensor, i: Int) {}
    }

    override fun onResume() {
        super.onResume()
        if (mLinearAccelerometer != null) {
            mSensorManager!!.registerListener(
                shakeListener,
                mLinearAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (mLinearAccelerometer != null) {
            mSensorManager!!.unregisterListener(shakeListener)
        }
    }

    private fun onDoubleClick(v : View?) {
        if (tracking) {
            findViewById<TextView>(R.id.tv_tracking_status).text = "TRACKING: OFF"
            findViewById<TextView>(R.id.tv_step_number).setTextColor(Color.parseColor("#656565"))
            findViewById<TextView>(R.id.tv_step_number).text = "0"

            if (mStepCounter != null) {
                mSensorManager?.unregisterListener(mListener)
            }

            // save current steps value
            val stepData: StepData? = viewModel.getStepData()
            if (stepData != null) {
                viewModel.updateStepData(StepData(steps = steps))
                findViewById<TextView>(R.id.tv_prev_steps).text =
                    "Last Tracked Steps: " + (viewModel.getStepData()?.steps ?: 0)
            } else { // first time saving step data
                viewModel.insertStepData(StepData(steps = steps))
                findViewById<TextView>(R.id.tv_prev_steps).text =
                    "Last Tracked Steps: " + (viewModel.getStepData()?.steps ?: 0)
            }
            steps = 0
            tracking = false
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < 300) {
            onDoubleClick(v)
        }
        lastClickTime = clickTime
    }

}
