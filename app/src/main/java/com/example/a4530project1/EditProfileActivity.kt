package com.example.a4530project1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
        val userJSON = File(filesDir,"userData.txt").readText(Charsets.UTF_8)

        val mapper = jacksonObjectMapper()
        val userFromJSON: User = mapper.readValue(userJSON)
        Log.d("D", userFromJSON.toString())

        findViewById<EditText>(R.id.et_name).setText(userFromJSON.name)
        findViewById<EditText>(R.id.et_age).setText(userFromJSON.age.toString())
        findViewById<EditText>(R.id.et_city).setText(userFromJSON.city)
        findViewById<EditText>(R.id.et_country).setText(userFromJSON.country)
        findViewById<EditText>(R.id.et_height).setText(userFromJSON.height)
        findViewById<EditText>(R.id.et_weight).setText(userFromJSON.weight.toString())
        findViewById<EditText>(R.id.et_sex).setText(userFromJSON.sex)
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
                    val height = findViewById<EditText>(R.id.et_height).text.toString()
                    val weight = findViewById<EditText>(R.id.et_weight).text.toString().toInt()
                    val sex = findViewById<EditText>(R.id.et_sex).text.toString()
                    val user = User(name, age, city, country, height, weight, sex)

                    val mapper = jacksonObjectMapper()
                    val userJson = mapper.writeValueAsString(user)

                    File(filesDir,"userData.txt").printWriter().use { out ->
                        out.println(userJson)
                    }

                    val intent = Intent(this@EditProfileActivity, ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}

