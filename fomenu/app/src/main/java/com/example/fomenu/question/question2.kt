package com.example.fomenu.question

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.fomenu.question.UserProfile
import com.example.fomenu.R
import com.google.android.material.snackbar.Snackbar

class question2 : AppCompatActivity() {
    override fun onBackPressed() {
        // Do nothing to block the back press
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question2)
        val cvGWeight = findViewById<CardView>(R.id.cvGainWeight)
        val cvMWeight = findViewById<CardView>(R.id.cvMaintainWeight)
        val cvLWeight = findViewById<CardView>(R.id.cvLoseWeight)
        val btnNext = findViewById<Button>(R.id.btnNextQuestion)
        val originalTint = ContextCompat.getColorStateList(this, R.color.card_color)

        Log.d("UserProfile", "Before getting UserProfile")
        val userProfile = intent.getParcelableExtra<UserProfile>("userProfile")
        Log.d("UserProfile", "After getting UserProfile")

        if(userProfile != null){
            Log.d("UserProfile", "Data received successfully: $userProfile")
        } else {
            Log.e("UserProfile", "Data not received")
        }

        var activeCardView : CardView? = null

        btnNext.setOnClickListener {
            if (activeCardView != null) {
                userProfile?.bodyweight_goal = if (activeCardView == cvGWeight) "Gain weight" else if (activeCardView == cvMWeight) "Maintain weight" else "Lose weight"
                val intent = Intent(this,question3_2::class.java)
                intent.putExtra("userProfile", userProfile)
                startActivity(intent)
                finish()
            }
            else{
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "Please select one of the above!", Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    // You can add any action you want here, or leave it empty
                }
                snackbar.show()
                false
            }
        }

        cvGWeight.setOnClickListener {
            activeCardView = cvGWeight
            val pressedBgColor = ContextCompat.getColor(this, R.color.btnQuestionPressedColor)
            cvGWeight.backgroundTintList = null
            cvGWeight.setBackgroundColor(pressedBgColor)
            if (activeCardView == cvGWeight) {
                cvMWeight.backgroundTintList = originalTint
                cvLWeight.backgroundTintList = originalTint
            }
        }
        cvMWeight.setOnClickListener {
            activeCardView = cvMWeight
            val pressedBgColor = ContextCompat.getColor(this, R.color.btnQuestionPressedColor)
            cvMWeight.backgroundTintList = null
            cvMWeight.setBackgroundColor(pressedBgColor)
            if (activeCardView == cvMWeight) {
                cvGWeight.backgroundTintList = originalTint
                cvLWeight.backgroundTintList = originalTint
            }
        }
        cvLWeight.setOnClickListener {
            activeCardView = cvLWeight
            val pressedBgColor = ContextCompat.getColor(this, R.color.btnQuestionPressedColor)
            cvLWeight.backgroundTintList = null
            cvLWeight.setBackgroundColor(pressedBgColor)
            if (activeCardView == cvLWeight) {
                cvGWeight.backgroundTintList = originalTint
                cvMWeight.backgroundTintList = originalTint
            }
        }



    }
}