package com.example.fomenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class workout_preview_activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_preview)


        val position = intent.getIntExtra("position", -1)
        val itemText = intent.getStringExtra("itemText")

        Log.d("WorkoutLog", "Received Position: $position")
        Log.d("WorkoutLog", "Received ItemText: $itemText")

        val textViewPosition = findViewById<TextView>(R.id.tvPosition)
        val nameText = findViewById<TextView>(R.id.nameTextView)
        val typeText = findViewById<TextView>(R.id.typeTextView)
        val diffText = findViewById<TextView>(R.id.difficultyTextView)
        val roundText = findViewById<TextView>(R.id.roundTextView)

        if (position != -1 && itemText != null) {
            val workout = getWorkouts(itemText)
            val selectedWorkout = workout.getOrNull(position)
            if (selectedWorkout != null) {
                val recyclerView: RecyclerView = findViewById(R.id.workoutRV)
                recyclerView.layoutManager = LinearLayoutManager(this)
                val adapter = MyWorkoutAdapter(selectedWorkout, supportFragmentManager)
                recyclerView.adapter = adapter

                val workoutName = "${selectedWorkout.name} ${position + 1}"
                textViewPosition.text = workoutName
                nameText.text = selectedWorkout.name
                diffText.text = "Difficulty: ${selectedWorkout.difficulty}"
                typeText.text = "Type: ${selectedWorkout.type}"
                roundText.text = "Rounds: ${selectedWorkout.rounds.toString()}"

                val startButton = findViewById<Button>(R.id.startWorkoutBtn)
                startButton.setOnClickListener {
                    val intent = Intent(this, TheWorkout::class.java).apply {
                        putExtra("workout", selectedWorkout)
                    }
                    startActivity(intent)
                }
            } else {
                textViewPosition.text = "Position: Not Provided"
            }
        } else {
            textViewPosition.text = "Position: Not Provided"
        }

    }
    private fun getWorkouts(itemText:String): List<Workout> {
        // Return a list of Workout objects (you can fill this with your data)
        return when (itemText) {
            "Full Body B" -> listOf(
                //done
                Proba_workout,Beginner_Full_Body_workout1,Beginner_Full_Body_workout2,Beginner_Full_Body_workout3,
                Beginner_Full_Body_workout4,Beginner_Full_Body_workout5,Beginner_Full_Body_workout6,Beginner_Full_Body_workout7,
                Beginner_Full_Body_workout8,Beginner_Full_Body_workout9,Beginner_Full_Body_workout10, Beginner_Full_Body_workout11,
                Beginner_Full_Body_workout12,Beginner_Full_Body_workout13,Beginner_Full_Body_workout14,Beginner_Full_Body_workout15,
                Beginner_Full_Body_workout16,Beginner_Full_Body_workout17,Beginner_Full_Body_workout18,Beginner_Full_Body_workout19, Beginner_Full_Body_workout20
                )
            "Full Body I" -> listOf(
                //done
                Inter_Full_Body_workout1, Inter_Full_Body_workout2,Inter_Full_Body_workout3, Inter_Full_Body_workout4, Inter_Full_Body_workout5,
                Inter_Full_Body_workout6,Inter_Full_Body_workout7,Inter_Full_Body_workout8,Inter_Full_Body_workout9,Inter_Full_Body_workout10,
                Inter_Full_Body_workout11,Inter_Full_Body_workout12,Inter_Full_Body_workout13,Inter_Full_Body_workout14,Inter_Full_Body_workout15,
                Inter_Full_Body_workout16,Inter_Full_Body_workout17,Inter_Full_Body_workout18,Inter_Full_Body_workout19,Inter_Full_Body_workout20
            )
            "Full Body A" -> listOf(
                //done
                Advanced_Full_Body_workout1, Advanced_Full_Body_workout2,Advanced_Full_Body_workout3, Advanced_Full_Body_workout4, Advanced_Full_Body_workout5,
                Advanced_Full_Body_workout6,Advanced_Full_Body_workout7,Advanced_Full_Body_workout8,Advanced_Full_Body_workout9,Advanced_Full_Body_workout10,

            )
            "Ab B" -> listOf(
                //done
                Beginner_Ab_workout1,Beginner_Ab_workout2,Beginner_Ab_workout3,Beginner_Ab_workout4,Beginner_Ab_workout1N,Beginner_Ab_workout5,
                Beginner_Ab_workout2N, Beginner_Ab_workout6, Beginner_Ab_workout7,Beginner_Ab_workout3N,Beginner_Ab_workout8,Beginner_Ab_workout4N,
                Beginner_Ab_workout9,Beginner_Ab_workout5N,Beginner_Ab_workout10,
            )
            "Ab I" -> listOf(
                //done
                Inter_Ab_workout1,Inter_Ab_workout2,Inter_Ab_workout3,Inter_Ab_workout1N,Inter_Ab_workout4,Inter_Ab_workout5,
                Inter_Ab_workout2N, Inter_Ab_workout6,Inter_Ab_workout3N, Inter_Ab_workout7,Inter_Ab_workout8,Inter_Ab_workout4N,
                Inter_Ab_workout9,Inter_Ab_workout10,Inter_Ab_workout5N
            )
            "Ab A" -> listOf(
                //done
                Advanced_Ab_workout1,Advanced_Ab_workout2,Advanced_Ab_workout1N,Advanced_Ab_workout3,Advanced_Ab_workout4, Advanced_Ab_workout2N,
                Advanced_Ab_workout5, Advanced_Ab_workout6,Advanced_Ab_workout3N, Advanced_Ab_workout7,Advanced_Ab_workout8,
                Advanced_Ab_workout9,Advanced_Ab_workout4N,Advanced_Ab_workout10,Advanced_Ab_workout5N
            )
            "Push B" -> listOf(
                //done
                Beginner_Push_workout1,Beginner_Push_workout2,Beginner_Push_workout3,Beginner_Push_workout4,Beginner_Push_workout5, Beginner_Push_workout1N,
                Beginner_Push_workout6,Beginner_Push_workout2N, Beginner_Push_workout7,Beginner_Push_workout3N,Beginner_Push_workout8, Beginner_Push_workout4N,
                Beginner_Push_workout9,Beginner_Push_workout5N,Beginner_Push_workout10,Beginner_Push_workout6N,Beginner_Push_workout11,Beginner_Push_workout7N,Beginner_Push_workout12,
                Beginner_Push_workout8N,Beginner_Push_workout13,Beginner_Push_workout9N,Beginner_Push_workout14,Beginner_Push_workout10N,Beginner_Push_workout15,Beginner_Push_workout16,
                Beginner_Push_workout17,Beginner_Push_workout18,Beginner_Push_workout19,Beginner_Push_workout20
            )
            "Push I" -> listOf(
                //done
                Inter_Push_workout1,Inter_Push_workout2,Inter_Push_workout3,Inter_Push_workout4,Inter_Push_workout5, Inter_Push_workout6,
                Inter_Push_workout1N,Inter_Push_workout7,Inter_Push_workout2N,Inter_Push_workout8, Inter_Push_workout9, Inter_Push_workout3N,
                Inter_Push_workout10,Inter_Push_workout11,Inter_Push_workout12, Inter_Push_workout13,Inter_Push_workout14,
                Inter_Push_workout15,Inter_Push_workout16, Inter_Push_workout17,Inter_Push_workout4N,Inter_Push_workout18,
                Inter_Push_workout5N,Inter_Push_workout19,Inter_Push_workout20
            )
            "Push A" -> listOf(
                //done
                Advanced_Push_workout1,Advanced_Push_workout2,Advanced_Push_workout3,Advanced_Push_workout1N,
                Advanced_Push_workout4,Advanced_Push_workout2N,Advanced_Push_workout5,Advanced_Push_workout3N,Advanced_Push_workout6, Advanced_Push_workout7,
                Advanced_Push_workout4N,Advanced_Push_workout8,Advanced_Push_workout9,Advanced_Push_workout10,Advanced_Push_workout5N
            )
            "Pull B" -> listOf(
                //done
                Beginner_Pull_workout1,Beginner_Pull_workout2,Beginner_Pull_workout3,Beginner_Pull_workout4,Beginner_Pull_workout5,Beginner_Pull_workout1N,
                Beginner_Pull_workout6,Beginner_Pull_workout2N, Beginner_Pull_workout7,Beginner_Pull_workout3N,Beginner_Pull_workout8,Beginner_Pull_workout4N,
                Beginner_Pull_workout9,Beginner_Pull_workout5N,Beginner_Pull_workout10,Beginner_Pull_workout6N,Beginner_Pull_workout11,Beginner_Pull_workout7N,
                Beginner_Pull_workout12,Beginner_Pull_workout8N,Beginner_Pull_workout13,Beginner_Pull_workout9N,Beginner_Pull_workout14,Beginner_Pull_workout10N,
                Beginner_Pull_workout15,Beginner_Pull_workout11N,Beginner_Pull_workout16,Beginner_Pull_workout12N,Beginner_Pull_workout17,Beginner_Pull_workout13N,
                Beginner_Pull_workout18,Beginner_Pull_workout14N,Beginner_Pull_workout19,Beginner_Pull_workout15N, Beginner_Pull_workout20
            )
            "Pull I" -> listOf(
                //done
                Inter_Pull_workout1,Inter_Pull_workout2,Inter_Pull_workout3,Inter_Pull_workout1N,Inter_Pull_workout4,Inter_Pull_workout2N,Inter_Pull_workout5,
                Inter_Pull_workout3N,Inter_Pull_workout6,Inter_Pull_workout4N,Inter_Pull_workout7, Inter_Pull_workout5N,Inter_Pull_workout8,Inter_Pull_workout9,
                Inter_Pull_workout10,Inter_Pull_workout3N,Inter_Pull_workout11,Inter_Pull_workout12,Inter_Pull_workout4N,Inter_Pull_workout13,Inter_Pull_workout14,Inter_Pull_workout15,
                Inter_Pull_workout16, Inter_Pull_workout17,Inter_Pull_workout18,Inter_Pull_workout19,Inter_Pull_workout5N,Inter_Pull_workout20,

                )
            "Pull A" -> listOf(
                //done
                Advanced_Pull_workout1,Advanced_Pull_workout1N,Advanced_Pull_workout2,Advanced_Pull_workout3,Advanced_Pull_workout2N,Advanced_Pull_workout4,
                Advanced_Pull_workout5,Advanced_Pull_workout3N,Advanced_Pull_workout6, Advanced_Pull_workout7,Advanced_Pull_workout8,Advanced_Pull_workout4N,
                Advanced_Pull_workout9, Advanced_Pull_workout10,Advanced_Pull_workout5N
            )
            "Leg B" -> listOf(
                //done
                Beginner_Leg_workout1,Beginner_Leg_workout2,Beginner_Leg_workout3,Beginner_Leg_workout4,Beginner_Leg_workout5,
                Beginner_Leg_workout6, Beginner_Leg_workout7,Beginner_Leg_workout8,Beginner_Leg_workout1N,
                Beginner_Leg_workout9,Beginner_Leg_workout2N,Beginner_Leg_workout10,Beginner_Leg_workout3N,Beginner_Leg_workout4N,Beginner_Leg_workout5N
            )
            "Leg I" -> listOf(
                //done
                Inter_Leg_workout1,Inter_Leg_workout2,Inter_Leg_workout3,Inter_Leg_workout4,Inter_Leg_workout5,
                Inter_Leg_workout6, Inter_Leg_workout7,Inter_Leg_workout8,Inter_Leg_workout1N,
                Inter_Leg_workout9,Inter_Leg_workout2N,Inter_Leg_workout10,Inter_Leg_workout3N,Inter_Leg_workout4N,Inter_Leg_workout5N
            )
            "Leg A" -> listOf(
                //done
                Advanced_Leg_workout1,Advanced_Leg_workout2,Advanced_Leg_workout3,Advanced_Leg_workout1N,Advanced_Leg_workout4,Advanced_Leg_workout5,Advanced_Leg_workout2N,
                Advanced_Leg_workout6, Advanced_Leg_workout3N,Advanced_Leg_workout7,Advanced_Leg_workout8,Advanced_Leg_workout4N, Advanced_Leg_workout9,
                Advanced_Leg_workout10,Advanced_Leg_workout5N
            )
            else -> listOf(
                //exception
            )
        }
    }
}