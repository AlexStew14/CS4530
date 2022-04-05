package com.example.a4530project1

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.LocationManager
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.File



class DataViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private var user: UserRepository = UserRepository(context)

    fun getPersonalData() : User? {
        var res : User? = null
        runBlocking {
            res = user.getUserData()
        }
        return res
    }

    fun updatePersonalData(data: User) {
        runBlocking {
            user.updateUserData(data)
        }
    }

    fun insertPersonalData(data: User) {
        runBlocking {
            user.insertUserData(data)
        }
    }

    fun getFitnessGoalData() : FitnessGoal? {
        var res : FitnessGoal? = null
        runBlocking {
            res = user.getFitnessData()
        }
        return res
    }

    fun insertFitnessData(data: FitnessGoal) {
        runBlocking {
            user.insertFitnessData(data)
        }
    }

    fun updateFitnessGoalData(data: FitnessGoal) {
        runBlocking {
            user.updateFitnessData(data)
        }
    }

    fun getWeatherData(locMag : LocationManager, cityTV : TextView, tempTV : TextView, statusTV : TextView) {
        runBlocking {
            user.getWeatherData(locMag, cityTV, tempTV, statusTV)
        }
    }

}