package com.example.a4530project1

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.launch
import java.io.File



class DataViewModel(val context: Context) : ViewModel() {

    private var user: UserRepository = UserRepository(context)
    private var weather: WeatherRepository = WeatherRepository()

    fun getPersonalData() : User {
        return user.getUserData()
    }

    fun updatePersonalData(data: User) {
        viewModelScope.launch {
            user.updateUserData(data)
        }
    }

    fun loadPersonalData() {
        viewModelScope.launch {
            user.loadUserData()
        }
    }

    fun getFitnessGoalData() : FitnessGoal {
        return user.getFitnessData()
    }

    fun updateFitnessGoalData(data: FitnessGoal) {
        viewModelScope.launch {
            user.updateFitnessData(data)
        }
    }

    fun loadFitnessGoalData() {
        viewModelScope.launch {
            user.loadFitnessData()
        }
    }
}