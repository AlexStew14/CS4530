package com.example.a4530project1

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import android.provider.MediaStore
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.runBlocking


class EditProfileActivity : AppCompatActivity(), View.OnClickListener{
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Uri? = result.data?.data
                findViewById<ImageView>(R.id.img_profile_picture_edit).setImageURI(data)
                findViewById<ImageView>(R.id.img_profile_picture_edit).setTag(data.toString())
            }
        }

    private lateinit var viewModel : DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        findViewById<Button>(R.id.btn_edit_profile_submit).setOnClickListener(this)
        findViewById<Button>(R.id.btn_profile_picture_select).setOnClickListener(this)


        viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        val personalData: User? = viewModel.getPersonalData()

        if (personalData != null) {
            findViewById<EditText>(R.id.et_name).setText(personalData.name)
            findViewById<EditText>(R.id.et_age).setText(personalData.age.toString())
            findViewById<EditText>(R.id.et_city).setText(personalData.city)
            findViewById<EditText>(R.id.et_country).setText(personalData.country)
            findViewById<EditText>(R.id.et_weight).setText(personalData.weight.toString())
            if (personalData.profilePicture.isNotEmpty()) {
                findViewById<ImageView>(R.id.img_profile_picture_edit).setImageURI(
                    Uri.parse(
                        personalData.profilePicture
                    )
                )
                findViewById<ImageView>(R.id.img_profile_picture_edit).setTag(personalData.profilePicture)
            }


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
                height_spinner.setSelection(adapter.getPosition(personalData.height))
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
                sex_spinner.setSelection(adapter.getPosition(personalData.sex))
            }
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
                    val age = findViewById<EditText>(R.id.et_age).text.toString()
                    val city = findViewById<EditText>(R.id.et_city).text.toString()
                    val country = findViewById<EditText>(R.id.et_country).text.toString()
                    val height = findViewById<Spinner>(R.id.sp_height).selectedItem.toString()
                    val weight = findViewById<EditText>(R.id.et_weight).text.toString()
                    val sex = findViewById<Spinner>(R.id.sp_sex).selectedItem.toString()
                    var profilePicture = ""
                    if (findViewById<ImageView>(R.id.img_profile_picture_edit).getTag() != null) {
                        profilePicture = findViewById<ImageView>(R.id.img_profile_picture_edit).getTag().toString()
                    }
                    val user = User(name=name, age=age, city=city, country=country, height=height, weight=weight, sex=sex, profilePicture=profilePicture)

                    viewModel.updatePersonalData(user)
                    
                    finish()
                }
            }
            R.id.btn_profile_picture_select -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2)
                }
                else{
                    SetProfileImage()
                }
            }
        }
    }

    private fun SetProfileImage(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(gallery)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                SetProfileImage()
            }
        }
    }

}

