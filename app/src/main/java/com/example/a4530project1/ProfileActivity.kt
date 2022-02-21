package com.example.a4530project1

import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        findViewById<Button>(R.id.btn_profile_edit).setOnClickListener(this)


        // TODO fix image stuff
//        val img_file = File(filesDir,"profilePicture.png")
//        if (!img_file.exists()) {
//            Thread(Runnable {
//                val img_default_file = File(filesDir,"DefaultProfilePicture.png")
//                val source = ImageDecoder.createSource(img_default_file)
//                val drawable = ImageDecoder.decodeDrawable(source)
//                findViewById<ImageView>(R.id.img_profile_picture).setImageDrawable(drawable)
//            }).start()
//        }
//        else {
//            Thread(Runnable {
//                val source = ImageDecoder.createSource(img_file)
//                val drawable = ImageDecoder.decodeDrawable(source)
//                findViewById<ImageView>(R.id.img_profile_picture).setImageDrawable(drawable)
//            }).start()
//        }
    }

    override fun onResume() {
        super.onResume()

        //TODO check if file exists
        val file = File(filesDir,"userData.txt")

        if (file.exists()) {
            val userJSON = file.readText(Charsets.UTF_8)

            val mapper = jacksonObjectMapper()
            val userFromJSON: User = mapper.readValue(userJSON)

            findViewById<TextView>(R.id.tv_name).text = userFromJSON.name
            findViewById<TextView>(R.id.tv_age).text = userFromJSON.age.toString()
            findViewById<TextView>(R.id.tv_city).text = userFromJSON.city
            findViewById<TextView>(R.id.tv_country).text = userFromJSON.country
            findViewById<TextView>(R.id.tv_height).text = userFromJSON.height
            findViewById<TextView>(R.id.tv_weight).text = userFromJSON.weight.toString()
            findViewById<TextView>(R.id.tv_sex).text = userFromJSON.sex
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_profile_edit -> {
                val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }
}