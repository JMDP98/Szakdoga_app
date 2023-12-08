package com.example.fomenu

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.room.Room
import com.example.fomenu.database.StepCountEntity
import com.example.fomenu.database.StepsDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, StepResetService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }
    }
}



class StepResetReceiver : BroadcastReceiver() {

    private val serviceScope = CoroutineScope(Dispatchers.Default)


    override fun onReceive(context: Context?, intent: Intent?) {

            if (context != null && intent != null) {
                // Your existing code...
            } else {
                Log.e("StepResetReceiver", "Received null context or intent")
            }
            if (context == null) {
                Log.e("StepResetReceiver", "Context is null")
                return
            }

            Log.d("StepResetReceiver", "onReceive triggered")
            val sharedPreferences = context?.getSharedPreferences("steps", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()

            val previousTotalSteps = sharedPreferences?.getFloat("key1", 0f) ?: 0f
            val totalSteps = sharedPreferences?.getFloat("totalSteps", 0f) ?: 0f

        val yesterday = Calendar.getInstance()
        yesterday.timeInMillis = System.currentTimeMillis()
        yesterday.add(Calendar.DAY_OF_YEAR,-1)

        val yesterdayDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(yesterday.time)
        val previousDayStepCount = totalSteps.toInt()-previousTotalSteps.toInt()

        val stepData = hashMapOf(
            "date" to yesterdayDateString,
            "stepCount" to previousDayStepCount
        )
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            FirebaseFirestore.getInstance().collection("users")
                .document(userId).collection("stepCounts")
                .add(stepData)
                .addOnSuccessListener { documentReference ->
                    Log.d("StepResetReceiver", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("StepResetReceiver", "Error adding document", e)
                }
        } else {
            //there is no authenticated user
        }

        editor?.putFloat("totalSteps", 0f) // Update totalSteps if needed
        editor?.putFloat("key1", totalSteps) // Reset the steps

        editor?.apply()

        val newPreviousTotalSteps = sharedPreferences?.getFloat("key1", 0f)
        val newTotalSteps = sharedPreferences?.getFloat("totalSteps", 0f)
        Log.d("StepResetReceiver", "Reset complete. New previousTotalSteps: $newPreviousTotalSteps, New totalSteps: $newTotalSteps")

        val resetCompleteIntent = Intent(StepResetService.ACTION_RESET_COMPLETED)
        context.sendBroadcast(resetCompleteIntent)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val resetIntent = Intent(context, StepResetReceiver::class.java).apply {
            action = "com.example.fomenu.STEP_RESET_ACTION"
        }
        val pending = PendingIntent.getBroadcast(context, 0, resetIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 10)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pending)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pending)
        }
    }
}



