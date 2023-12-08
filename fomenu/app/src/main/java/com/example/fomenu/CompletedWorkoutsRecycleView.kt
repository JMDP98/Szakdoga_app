package com.example.fomenu

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CompletedWorkoutsRecycleViewAdapter(private val context: Context) :
    RecyclerView.Adapter<CompletedWorkoutsViewHolder>() {

    private var completedWorkouts = mutableListOf<CompletedWorkout>()


    init {
        loadCompletedWorkoutsFromFirestore { success, workouts ->
            if (success) {
                this.completedWorkouts.clear()
                this.completedWorkouts.addAll(workouts)
                notifyDataSetChanged()
            } else {
                // Handle error, could not load data
            }
        }
    }
    private fun createWorkoutFromMap(workoutMap: Map<String, Any>): Workout? {
        // Check for all required fields
        val name = workoutMap["name"] as? String
        val difficulty = workoutMap["difficulty"] as? String
        val type = workoutMap["type"] as? String
        val rounds = (workoutMap["rounds"] as? Number)?.toInt()
        val rest = workoutMap["rest"] as? String
        val position = (workoutMap["position"] as? Number)?.toInt()
        val exercises = (workoutMap["exercises"] as? List<Map<String, Any>>)?.map { exerciseMap ->
            Log.d("CompletedWorkoutsAdapter", "Exercise map: $exerciseMap")
            val exercise = Exercise(
                // Assuming Exercise has a constructor matching these fields
                name = exerciseMap["name"] as? String ?: "",
                repetitionCount = exerciseMap["repetitionCount"] as? String ?: "",
                picturePath = exerciseMap["picturePath"] as? String ?: "",
                videoPath = exerciseMap["videoPath"] as? String ?: ""
            )
            Log.d("CompletedWorkoutsAdapter", "Created exercise: $exercise")
            exercise
        } ?: listOf()
        val identifier = (workoutMap["idetifier"] as? Number)?.toInt()

        // If any of the required fields are null, return null
        if (name == null || difficulty == null || type == null || rounds == null || rest == null || position == null || identifier == null) {
            return null
        }

        // Create and return the Workout object
        return Workout(name, difficulty, type, rounds, rest, position, exercises, identifier)
    }



    private fun loadCompletedWorkoutsFromFirestore(callback: (Boolean, List<CompletedWorkout>) -> Unit) {
        Log.d("Adapter", "Loading completed workouts from Firestore")

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            val collectionRef = FirebaseFirestore.getInstance().collection("users").document(userId)
                .collection("completedWorkouts")
            collectionRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    Log.d("CompletedWorkoutsAdapter", "Fetched documents count: ${documents?.size ?: "null"}")
                    val completedWorkouts = documents?.mapNotNull { document ->
                        Log.d("CompletedWorkoutsAdapter", "Document data: ${document.data}")

                        val workoutData = document.data as? Map<String, Any>
                        workoutData?.let { data ->
                            val workout = createWorkoutFromMap(data["workout"] as Map<String, Any>)
                            val completionDate = data["completionDate"] as? String
                            Log.d("CompletedWorkoutsAdapter", "Created workout from map: $workout")

                            if (workout != null && completionDate != null) {
                                CompletedWorkout(workout, completionDate)
                            } else {
                                null
                            }
                        }
                    } ?: listOf()
                    Log.d("CompletedWorkoutsAdapter", "Number of workouts loaded: ${completedWorkouts.size}")

                    callback(true, completedWorkouts)
                } else {
                    Log.e("CompletedWorkoutsAdapter", "Error loading workouts", task.exception)

                    callback(false, listOf())
                }
            }
        } ?: run {
            callback(false, listOf())
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedWorkoutsViewHolder {
        Log.d("CompletedWorkoutsAdapter", "onCreateViewHolder called")
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.your_workouts_frg_rv_list_item, parent, false)
        return CompletedWorkoutsViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return completedWorkouts.size
    }

    override fun onBindViewHolder(holder: CompletedWorkoutsViewHolder, position: Int) {
        val completedWorkout = completedWorkouts[position]
        Log.d("CompletedWorkoutsAdapter", "Binding ViewHolder at position $position with data: ${completedWorkout.workout}")
        holder.bind(completedWorkout.workout, completedWorkout.completionDate)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, WorkoutPreviewRecommended::class.java)
            // Make sure 'clickedWorkout' expects a Parcelable object, otherwise adjust accordingly.
            intent.putExtra("workout", completedWorkout.workout)
            context.startActivity(intent)
        }
    }
}

class CompletedWorkoutsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(workout: Workout, completionDate: String) {
        val myTextView = view.findViewById<TextView>(R.id.tvFull_body_list_item)
        myTextView.text = workout.name
        val myDateTextView = view.findViewById<TextView>(R.id.tvDate)
        myDateTextView.text = completionDate
    }
}
