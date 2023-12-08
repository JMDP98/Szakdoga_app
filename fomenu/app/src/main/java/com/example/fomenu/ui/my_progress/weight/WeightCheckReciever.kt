package com.example.fomenu.ui.my_progress.weight

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.fomenu.MainActivity
import com.example.fomenu.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WeightCheckReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        checkTodaysEntryExists { entryExists ->
            if (!entryExists) {
                showNotification(context)
            }
        }
    }
}

private fun showNotification(context: Context) {
    val notificationIntent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("openFragment", "Weight")
    }
    val pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, pendingIntentFlag)

    val builder = NotificationCompat.Builder(context, "weights_channel_id")
        .setSmallIcon(androidx.core.R.drawable.notification_action_background) // Replace with your actual notification icon
        .setContentTitle("Weight Reminder")
        .setContentText("Don't forget to add your weight today!")
        .setSmallIcon(R.drawable.rsz_pullup_icon)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(longArrayOf(0, 1000))

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notify(1, builder.build())
    }
}
private fun checkTodaysEntryExists(callback: (Boolean) -> Unit) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()
    val todayDate = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(Calendar.getInstance().time)

    currentUser?.let { user ->
        db.collection("users")
            .document(user.uid)
            .collection("weights")
            .whereEqualTo("date", todayDate)
            .whereEqualTo("flag", true)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    // Entry for today exists
                    callback(true)
                } else {
                    // No entry for today
                    callback(false)
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error checking today's entry", e)
                callback(false)
            }
    } ?: callback(false) // Handle the case where currentUser is null
}

