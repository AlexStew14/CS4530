package com.example.a4530project1

import android.graphics.ImageDecoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        findViewById<Button>(R.id.btn_sign_in_submit).setOnClickListener(this)

        val height_spinner: Spinner = findViewById(R.id.sp_height)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.heights_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            height_spinner.adapter = adapter
            //height_spinner.setSelection(adapter.getPosition(userFromJSON.height))
        }

        val sex_spinner: Spinner = findViewById(R.id.sp_sex)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.sex_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            sex_spinner.adapter = adapter
            //sex_spinner.setSelection(adapter.getPosition(userFromJSON.sex))
        }
    }
    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_sign_in_submit -> {
                val name = findViewById<EditText>(R.id.et_name).text.toString()
                if (name.isEmpty()) {
                    val text = "Must enter name"
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()
                }
                else {
                    val name = findViewById<EditText>(R.id.et_name).text.toString()
                    val age = findViewById<EditText>(R.id.et_age).text.toString().toInt()
                    val city = findViewById<EditText>(R.id.et_city).text.toString()
                    val country = findViewById<EditText>(R.id.et_country).text.toString()
                    val height = findViewById<Spinner>(R.id.sp_height).selectedItem.toString()
                    val weight = findViewById<EditText>(R.id.et_weight).text.toString().toInt()
                    val sex = findViewById<Spinner>(R.id.sp_sex).selectedItem.toString()
                    val user = User(name, age, city, country, height, weight, sex)

                    val mapper = jacksonObjectMapper()
                    val userJson = mapper.writeValueAsString(user)

                    File(filesDir,"userData.txt").printWriter().use { out ->
                        out.println(userJson)
                    }
                    finish()
                }
            }
        }
    }
}