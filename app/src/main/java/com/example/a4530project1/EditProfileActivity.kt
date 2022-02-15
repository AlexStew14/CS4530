package com.example.a4530project1

import android.app.Activity
import android.graphics.ImageDecoder
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File


data class User (
    val name: String,
    val age: Int,
    val city: String,
    val country: String,
    val height: String,
    val weight: Int,
    val sex: String
)

class EditProfileActivity : AppCompatActivity(), View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        findViewById<Button>(R.id.btn_edit_profile_submit).setOnClickListener(this)

        // get file contents
        // TODO check if file exists
        val userJSON = File(filesDir,"userData.txt").readText(Charsets.UTF_8)

        val mapper = jacksonObjectMapper()
        val userFromJSON: User = mapper.readValue(userJSON)

        findViewById<EditText>(R.id.et_name).setText(userFromJSON.name)
        findViewById<EditText>(R.id.et_age).setText(userFromJSON.age.toString())
        findViewById<EditText>(R.id.et_city).setText(userFromJSON.city)
        findViewById<EditText>(R.id.et_country).setText(userFromJSON.country)
        findViewById<EditText>(R.id.et_weight).setText(userFromJSON.weight.toString())

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
            height_spinner.setSelection(adapter.getPosition(userFromJSON.height))
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
            sex_spinner.setSelection(adapter.getPosition(userFromJSON.sex))
        }

        val img_file = File(filesDir,"profilePicture.png")
        if (!img_file.exists()) {
            Thread(Runnable {
                val img_default_file = File(filesDir,"DefaultProfilePicture.png")
                val source = ImageDecoder.createSource(img_default_file)
                val drawable = ImageDecoder.decodeDrawable(source)
                findViewById<ImageView>(R.id.img_profile_picture_edit).setImageDrawable(drawable)
            }).start()
        }
        else {
            Thread(Runnable {
                val source = ImageDecoder.createSource(img_file)
                val drawable = ImageDecoder.decodeDrawable(source)
                findViewById<ImageView>(R.id.img_profile_picture_edit).setImageDrawable(drawable)
            }).start()
        }


    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_edit_profile_submit -> {
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

