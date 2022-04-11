package com.example.a4530project1

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDatabaseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: User) : Long

    @Query("SELECT * from user_data_table WHERE userID = :key")
    fun getUser(key: Long): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: FitnessGoal) : Long

    @Query("SELECT * from fitness_goal_table WHERE fitnessID = :key")
    fun getFitness(key: Long): FitnessGoal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: WeatherData) : Long

    @Query("SELECT * from weather_table WHERE weatherID = :key")
    fun getWeather(key: Long): WeatherData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: StepData) : Long

    @Query("SELECT * from step_table WHERE stepID = :key")
    fun getStepData(key: Long): StepData?
}