package com.example.a4530project1

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.launch
import java.io.File

data class User (
    val name: String,
    val age: String,
    val city: String,
    val country: String,
    val height: String,
    val weight: String,
    val sex: String,
    val profilePicture: String
)

data class FitnessGoal (
    val weightGoal: String,
    val activityLevel: String,
    val poundsPerWeek: Float
)

class UserRepository(val context: Context) {

    private lateinit var userData: User
    private lateinit var fitnessData: FitnessGoal

    fun getUserData() : User {
        return userData
    }

    suspend fun updateUserData(data: User) {
        userData = data
        val mapper = jacksonObjectMapper()
        val userJson = mapper.writeValueAsString(userData)
        File(context.filesDir,"userData.txt").printWriter().use { out -> out.println(userJson) }

    }

    suspend fun loadUserData() {
        val file = File(context.filesDir,"userData.txt")
        if (file.exists()) {
            val userJSON = file.readText(Charsets.UTF_8)
            val mapper = jacksonObjectMapper()
            userData = mapper.readValue(userJSON)
        }
    }

    fun getFitnessData() : FitnessGoal {
        return fitnessData
    }

    suspend fun updateFitnessData(data: FitnessGoal) {
        fitnessData = data
        val mapper = jacksonObjectMapper()
        val userJson = mapper.writeValueAsString(userData)
        File(context.filesDir,"fitnessGoalData.txt").printWriter().use { out -> out.println(userJson) }
    }

    fun loadFitnessData() {
        val file = File(context.filesDir,"fitnessGoalData.txt")
        if (file.exists()) {
            val fitnessJSON = file.readText(Charsets.UTF_8)
            val mapper = jacksonObjectMapper()
            fitnessData = mapper.readValue(fitnessJSON)
        }
    }
}