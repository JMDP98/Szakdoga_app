package com.example.fomenu

import android.app.Application
import androidx.room.Room
import com.example.fomenu.database.StepsDatabase

class MyApplication : Application() {
    val stepsDatabase = Room.databaseBuilder(
        applicationContext,
        StepsDatabase::class.java, "step-count-database"
    ).build()
}
