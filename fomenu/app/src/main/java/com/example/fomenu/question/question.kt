package com.example.fomenu.question

import android.content.Intent
import com.google.android.material.snackbar.Snackbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.fomenu.question.UserProfile
import com.example.fomenu.R

class question : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        val buttonNext = findViewById<Button>(R.id.btnNextQuestion)
        val buttonMale = findViewById<ImageButton>(R.id.ibtnMale)
        val buttonFemale = findViewById<ImageButton>(R.id.ibtnFemale)
        val originalTintMale = buttonMale.backgroundTintList
        val originalTintFemale = buttonFemale.backgroundTintList
        val weightText = findViewById<EditText>(R.id.etWeight)
        val heightText = findViewById<EditText>(R.id.etHeight)
        val ageText = findViewById<EditText>(R.id.etAge)

        val userProfile = UserProfile("","","","","","", emptyList(),0,0,0)

        var activeButton: ImageButton? = null

        buttonNext.setOnClickListener {
            val weight= weightText.text.toString()
            val height = heightText.text.toString()
            val age = ageText.text.toString()
            if (validateInput(weight,height,age,activeButton) && activeButton != null) {
                userProfile.weight = weight
                userProfile.height = height
                userProfile.age = age
                userProfile.sex = if (activeButton == buttonMale) "Male" else "Female"
                val intent = Intent(this,question2::class.java)
                intent.putExtra("userProfile", userProfile)
                startActivity(intent)
                finish()
            }
        }



        buttonMale.setOnClickListener {
            activeButton = buttonMale
            val pressedBgColor = ContextCompat.getColor(this, R.color.btnMPressColor)
            buttonMale.backgroundTintList = null
            buttonMale.setBackgroundColor(pressedBgColor)
            if (activeButton == buttonMale) {
                buttonFemale.backgroundTintList = originalTintFemale
            }

        }
        buttonFemale.setOnClickListener {
            activeButton = buttonFemale
            val pressedBgColor = ContextCompat.getColor(this, R.color.btnFPressColor)
            buttonFemale.backgroundTintList = null
            buttonFemale.setBackgroundColor(pressedBgColor)
            if (activeButton == buttonFemale) {
                buttonMale.backgroundTintList = originalTintMale
            }


        }


    }
    override fun onBackPressed() {
        // Do nothing to block the back press
    }

    private fun validateInput(weight: String?, height: String?, age: String?, actBut : ImageButton?): Boolean {
        return when {
            weight.isNullOrEmpty() -> {
                //Toast.makeText(this, "Weight is empty", Toast.LENGTH_LONG).show()
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "Enter your weight!", Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    // You can add any action you want here, or leave it empty
                }
                snackbar.show()
                false
            }
            (weight.toInt() >= 180) -> {
                //Toast.makeText(this, "Age is empty", Toast.LENGTH_LONG).show()
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "I can't help you just try to walk around!", Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    // You can add any action you want here, or leave it empty
                }
                snackbar.show()
                false
            }
            (weight.toInt() <= 30) -> {
                //Toast.makeText(this, "Age is empty", Toast.LENGTH_LONG).show()
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "You need to gain some weight first!", Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    // You can add any action you want here, or leave it empty
                }
                snackbar.show()
                false
            }


            height.isNullOrEmpty() -> {
                //Toast.makeText(this, "Height is empty", Toast.LENGTH_LONG).show()
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "Enter your height!", Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    // You can add any action you want here, or leave it empty
                }
                snackbar.show()
                false
            }
            (height.toInt() >= 220) -> {
                //Toast.makeText(this, "Age is empty", Toast.LENGTH_LONG).show()
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "Are you a giraffe?", Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    // You can add any action you want here, or leave it empty
                }
                snackbar.show()
                false
            }
            (height.toInt() <= 120) -> {
                //Toast.makeText(this, "Age is empty", Toast.LENGTH_LONG).show()
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "You need to grow some more!", Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    // You can add any action you want here, or leave it empty
                }
                snackbar.show()
                false
            }

            age.isNullOrEmpty() -> {
                //Toast.makeText(this, "Age is empty", Toast.LENGTH_LONG).show()
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "Enter your age!", Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    // You can add any action you want here, or leave it empty
                }
                snackbar.show()
                false
            }
            (age.toInt() >= 100) -> {
                //Toast.makeText(this, "Age is empty", Toast.LENGTH_LONG).show()
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "Are you still alive?", Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    // You can add any action you want here, or leave it empty
                }
                snackbar.show()
                false
            }
            (age.toInt() <= 10) -> {
                //Toast.makeText(this, "Age is empty", Toast.LENGTH_LONG).show()
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "Come back when you're older!", Snackbar.LENGTH_LONG)
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