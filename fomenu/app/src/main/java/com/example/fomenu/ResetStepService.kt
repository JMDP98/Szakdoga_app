package com.example.fomenu

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.fomenu.database.StepCountEntity
import com.example.fomenu.database.StepsDao
import com.example.fomenu.database.StepsDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class StepResetService : Service(), SensorEventListener {

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private val serviceScope = CoroutineScope(Dispatchers.Default)

    private lateinit var stepsDatabase: StepsDatabase

    companion object {
        const val ACTION_RESET_COMPLETED = "com.example.fomenu.ACTION_RESET_COMPLETED"
    }

    private val resetReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_RESET_COMPLETED) {
                loadData()
                Log.d("StepResetService", "Reset broadcast received, data reloaded")
            }
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        loadData()
        Log.d("StepResetService", "onCreate called")

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            stopSelf()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(
                "step_channel_id",
                "Step Counter",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = applicationContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        val filter = IntentFilter(ACTION_RESET_COMPLETED)
        registerReceiver(resetReceiver, filter)

        try {

            // Set up an alarm to trigger at midnight
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 1)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 10)
                add(Calendar.DAY_OF_YEAR, 1) // Set for next day
            }

            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, StepResetReceiver::class.java)
            intent.action = "com.example.fomenu.STEP_RESET_ACTION"
            pendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }

            Log.d("StepResetService", "Alarm time set for: ${calendar.time}")

        } catch (e: Exception) {
            Log.e("StepResetService", "Error in setting alarm: ${e.message}")
            e.printStackTrace()
        }

    }

    private fun updateNotification(currentSteps: Int) {
        val notification = createNotification(currentSteps)
        val notificationManager =
            ContextCompat.getSystemService(this, NotificationManager::class.java)
        notificationManager?.notify(1, notification)
    }

    private fun createNotification(currentSteps: Int): Notification {
        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

        return NotificationCompat.Builder(applicationContext, "step_channel_id")
            .setContentTitle("Step Counter")
            .setContentText("Current Steps: $currentSteps")
            .setSmallIcon(R.drawable.rsz_pullup_icon) // Replace with your actual icon
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        loadData() // Ensure current step count is loaded
        val currentSteps = (totalSteps - previousTotalSteps).toInt()
        val notification = createNotification(currentSteps) // Initialize with 0 or load initial step count
        startForeground(1, notification) // Use the notification ID you use for updates
        Log.d("StepResetService", "onStartCommand called")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        if (::pendingIntent.isInitialized) {
            alarmManager.cancel(pendingIntent)
        }
        sensorManager?.unregisterListener(this)

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = event.values[0]
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            updateNotification(currentSteps)
            val intent = Intent("com.example.UPDATE_STEPS")
            intent.putExtra("stepCount", currentSteps)
            sendBroadcast(intent)
            Log.d("StepResetService", "Sensor Changed: currentSteps=$currentSteps")
            saveData()
        }
    }
    private fun saveData(){
        val sharedPreferences : SharedPreferences = getSharedPreferences("steps",Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putFloat("key1",previousTotalSteps)
        editor.putFloat("totalSteps", totalSteps)
        editor.apply()
        Log.d("StepResetService", "Data saved: previousTotalSteps=$previousTotalSteps, totalSteps=$totalSteps")

    }
    private fun loadData(){
        val sharedPreferences : SharedPreferences = getSharedPreferences("steps",Context.MODE_PRIVATE)
        val savedTotalSteps : Float = sharedPreferences.getFloat("totalSteps", 0f)
        val savedPreviousTotalSteps : Float = sharedPreferences.getFloat("key1",0f)

        previousTotalSteps = savedPreviousTotalSteps
        totalSteps = savedTotalSteps
        Log.d("StepResetService", "Data loaded: previousTotalSteps=$previousTotalSteps, totalSteps=$totalSteps")

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}

