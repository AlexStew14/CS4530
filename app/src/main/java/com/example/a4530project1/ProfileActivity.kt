package com.example.a4530project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        findViewById<Button>(R.id.btn_profile_edit).setOnClickListener(this)
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