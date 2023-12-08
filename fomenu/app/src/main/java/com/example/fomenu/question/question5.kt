package com.example.fomenu.question

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.example.fomenu.MainActivity
import com.example.fomenu.question.UserProfile
import com.example.fomenu.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class question5 : AppCompatActivity() {

    override fun onBackPressed() {
        // Do nothing to block the back press
    }

    private var userClickedNo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question5)
        val userProfile = intent.getParcelableExtra<UserProfile>("userProfile")
        val daysText = findViewById<EditText>(R.id.etDays)
        val maxPushText = findViewById<EditText>(R.id.etMaxPush)
        val maxPullText = findViewById<EditText>(R.id.etMaxPull)
        val buttonNext = findViewById<Button>(R.id.btnNextQuestion)


        if (userProfile != null) {
            Log.d("UserProfile", "Data received successfully: $userProfile")
        } else {
            Log.e("UserProfile", "Data not received")
        }
        if (userProfile != null) {
            buttonNext.setOnClickListener {
                val pull = maxPullText.text.toString()
                val push = maxPushText.text.toString()
                val days = daysText.text.toString()

                if (days.toInt() == 2 || days.toInt() == 1) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("I recommend to train more often.")
                    if (days.toInt() == 2) {
                        builder.setMessage("Are you sure you only want to train twice a week?")
                    }
                    if (days.toInt() == 1) {
                        builder.setMessage("Are you sure you only want to train once a week?")
                    }
                    builder.setPositiveButton("Yes") { dialog, which ->
                        dialog.dismiss()
                        userClickedNo = false // Reset the flag
                        // Continue with validation and next steps here
                        if (validateInput(days, this)) {
                            userProfile?.maxPull = pull.toIntOrNull() ?: 0
                            userProfile?.maxPush = push.toIntOrNull() ?: 0
                            userProfile?.daysToTrain = days.toInt()

                            val db = FirebaseFirestore.getInstance()
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            currentUser?.let {
                                db.collection("user_profiles")
                                    .document(currentUser.uid)
                                    .set(userProfile)
                                    .addOnSuccessListener {
                                        Log.d("UserProfile", "Data saved successfully")
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("UserProfile", "Error saving data: $exception")
                                    }
                            }

                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("userProfile", userProfile)
                            Log.d("UserProfile", "Data received successfully: $userProfile")
                            setFirstRunComplete(this)
                            clearDateToWorkoutMap(this)
                            startActivity(intent)
                            finish()
                        }
                    }

                    builder.setNegativeButton("No") { dialog, which ->
                        dialog.dismiss()
                        userClickedNo = true // Set the flag if the user clicks "No"
                    }

                    val dialog = builder.create()
                    dialog.show()
                } else {
                    if (validateInput(days, this)) {
                        userProfile?.maxPull = pull.toIntOrNull() ?: 0
                        userProfile?.maxPush = push.toIntOrNull() ?: 0
                        userProfile?.daysToTrain = days.toInt()

                        val db = FirebaseFirestore.getInstance()
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        currentUser?.let {
                            db.collection("user_profiles").document(currentUser.uid)
                                .set(userProfile)
                                .addOnSuccessListener {
                                    Log.d("UserProfile", "Data saved successfully")
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("UserProfile", "Error saving data: $exception")
                                }
                        }

                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("userProfile", userProfile)
                        Log.d("UserProfile", "Data received successfully: $userProfile")
                        setFirstRunComplete(this)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
    fun setFirstRunComplete(context: Context) {
        val sharedPreferences = context.getSharedPreferences("IsFirstRun", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("firstRun", false).apply()
        Log.d("LoginActivity", "First run set to complete")
        val updatedFirstRun = sharedPreferences.getBoolean("firstRun", true)
        Log.d("LoginActivity", "Updated firstRun value: $updatedFirstRun")


    }
    fun clearDateToWorkoutMap(context: Context) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("dateToWorkoutMap")
        editor.apply()
    }

    private fun validateInput(days: String?, activity: Activity): Boolean {
        return when {
            days.isNullOrEmpty() || days.toIntOrNull() == null -> {
                //Toast.makeText(this, "Age is empty", Toast.LENGTH_LONG).show()
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "Enter how many days you want to train!", Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    // You can add any action you want here, or leave it empty
                }
                snackbar.show()
                false
            }
            (days.toInt() >= 8) -> {
                //Toast.makeText(this, "Age is empty", Toast.LENGTH_LONG).show()
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "There's only 7 days a week as far as i know!", Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    // You can add any action you want here, or leave it empty
                }
                snackbar.show()
                false
            }
            (days.toInt() == 1) -> {
                //Toast.makeText(this, "Age is empty", Toast.LENGTH_LONG).show()
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "Train at least twice a week!", Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    // You can add any action you want here, or leave it empty
                }
                snackbar.show()
                false
            }
            (days.toInt() == 0) -> {
                //Toast.makeText(this, "Age is empty", Toast.LENGTH_LONG).show()
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "Enter how many days you want to train!", Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    // You can add any action you want here, or leave it empty
                }
                snackbar.show()
                false
            }

            else ->{
                true
            }

        }

    }
}
