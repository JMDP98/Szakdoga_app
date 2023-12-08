package com.example.fomenu.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StepCountEntity::class], version = 1)
abstract class StepsDatabase : RoomDatabase() {
    abstract fun stepCountDao(): StepsDao

}
