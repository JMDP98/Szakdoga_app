package com.example.fomenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.round

class WorkoutPreviewRecommended : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_preview)
        val selectedWorkout = intent.getParcelableExtra<Workout>("workout") ?: return

        val textViewPosition = findViewById<TextView>(R.id.tvPosition)
        val nameText = findViewById<TextView>(R.id.nameTextView)
        val diffText = findViewById<TextView>(R.id.difficultyTextView)
        val typeText = findViewById<TextView>(R.id.typeTextView)
        val roundText = findViewById<TextView>(R.id.roundTextView)


        val recyclerView: RecyclerView = findViewById(R.id.workoutRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = MyWorkoutAdapter(selectedWorkout, supportFragmentManager)
        recyclerView.adapter = adapter

        val workoutName = "${selectedWorkout.name}"
        textViewPosition.text = workoutName
        nameText.text = selectedWorkout.name
        diffText.text = "Difficulty: ${selectedWorkout.difficulty}"
        typeText.text = "Type: ${selectedWorkout.type}"
        roundText.text = "Rounds: ${selectedWorkout.rounds.toString()}"

        val startButton = findViewById<Button>(R.id.startWorkoutBtn)

        if (selectedWorkout.name == "Rest Day" || selectedWorkout.name == "Completed!" || selectedWorkout.name == "Adding soon") {
            // If it's a rest day, make the start button invisible
            startButton.visibility = View.INVISIBLE
        }
        startButton.setOnClickListener {
            val intent = Intent(this, TheWorkout::class.java).apply {
                putExtra("workout", selectedWorkout)
            }
            startActivity(intent)
        }


    }
}