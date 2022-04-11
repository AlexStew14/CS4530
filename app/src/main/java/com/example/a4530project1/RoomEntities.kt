package com.example.a4530project1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.reflect.Constructor
import java.util.*

@Entity(tableName = "user_data_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    val userID: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "age")
    val age: String,

    @ColumnInfo(name = "city")
    val city: String,

    @ColumnInfo(name = "country")
    val country: String,

    @ColumnInfo(name = "height")
    val height: String,

    @ColumnInfo(name = "weight")
    val weight: String,

    @ColumnInfo(name = "sex")
    val sex: String,

    @ColumnInfo(name = "profilePicture")
    val profilePicture: String
)

@Entity(tableName = "fitness_goal_table")
data class FitnessGoal  (
    @PrimaryKey(autoGenerate = true)
    val fitnessID: Long = 0,

    @ColumnInfo(name = "weightGoal")
    val weightGoal: String,

    @ColumnInfo(name = "activityLevel")
    val activityLevel: String,

    @ColumnInfo(name = "poundsPerWeek")
    val poundsPerWeek: Float
)

@Entity(tableName = "weather_table")
data class WeatherData  (
    @PrimaryKey(autoGenerate = true)
    val weatherID: Long = 0,

    @ColumnInfo(name = "temp")
    val temp: Int,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "city")
    val city: String
)

@Entity(tableName = "step_table")
data class StepData  (
    @PrimaryKey(autoGenerate = true)
    var stepID: Long = 0,

    @ColumnInfo(name = "steps")
    var steps: Int? = null
)