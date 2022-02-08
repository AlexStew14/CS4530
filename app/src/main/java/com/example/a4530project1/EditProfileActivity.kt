package com.example.a4530project1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class EditProfileActivity : AppCompatActivity(), View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        findViewById<Button>(R.id.btn_edit_profile_submit).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_edit_profile_submit -> {
                // TODO save data
                val name = findViewById<EditText>(R.id.et_name).text.toString()
                if (name.isEmpty()) {
                    
                }
            }
        }
    }
}

