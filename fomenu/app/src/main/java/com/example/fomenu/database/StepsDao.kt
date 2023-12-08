package com.example.fomenu.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fomenu.database.StepCountEntity


@Dao
interface StepsDao {
    @Insert
    fun insertStepCount(stepCount: StepCountEntity)

    @Query("SELECT * FROM step_count WHERE date = :date")
    fun getStepCountByDate(date: String): StepCountEntity?

    @Query("SELECT * FROM step_count")
    fun getAllStepCounts(): List<StepCountEntity>

    @Query("DELETE FROM step_count")
    fun deleteAll()

}
