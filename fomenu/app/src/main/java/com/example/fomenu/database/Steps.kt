package com.example.fomenu.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_count")
data class StepCountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int= 0,
    val date: String,
    val stepCount: Int
)
