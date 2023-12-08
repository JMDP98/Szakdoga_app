package com.example.fomenu.ui.my_progress

import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fomenu.MainActivity
import com.example.fomenu.R
import com.example.fomenu.StepCountAdapter
import com.example.fomenu.Workout
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.mikhaellopez.circularprogressbar.CircularProgressBar

data class StepCount(
    val date: String = "",
    val stepCount: Int = 0
)

class my_progress : Fragment() {

    companion object {
        fun newInstance() = my_progress()

        }


    private lateinit var viewModel: MyProgressViewModel
    private lateinit var tvStepsTaken : TextView
    private lateinit var listView : ListView
    private lateinit var weightButton: Button
    val db = FirebaseFirestore.getInstance()

    private lateinit var stepCountAdapter: StepCountAdapter
    private lateinit var recyclerView: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_my_progress, container, false)

        tvStepsTaken = view.findViewById(R.id.tvSteps)
        listView = view.findViewById(R.id.completedWorkoutsList)
        weightButton = view.findViewById(R.id.weightButton)
        recyclerView = view.findViewById(R.id.rvSteps)

        val list = listOf("Full Body Workouts","Pull Workouts","Push Workouts","Leg Workouts","Ab Workouts")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list.subList(0,5))
        listView.adapter = adapter

        weightButton.setOnClickListener {

            val navController = findNavController()

            navController.navigate(R.id.navigation_weight)
        }


        return view
    }
    private fun countWorkoutsByName(workouts: List<String>): Map<String, Int> {
        return workouts.groupingBy { it }.eachCount()
    }
    private fun fetchStepCounts(callback: (Boolean, List<StepCount>) -> Unit) {
        Log.d("StepCounts", "Fetching step counts...")
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val stepCountsCollectionRef = FirebaseFirestore.getInstance()
                .collection("users").document(currentUser.uid)
                .collection("stepCounts")
                .orderBy("date", Query.Direction.ASCENDING)
            stepCountsCollectionRef.get()
                .addOnSuccessListener { documents ->
                    Log.d("StepCounts", "Successfully fetched documents")
                    val stepCounts = documents.mapNotNull { document ->
                        document.toObject(StepCount::class.java)
                    }
                    Log.d("StepCounts", "Parsed ${stepCounts.size} step counts")
                    callback(true, stepCounts)
                }
                .addOnFailureListener { exception ->
                    Log.w("StepCounts", "Error getting documents: ", exception)
                    callback(false, emptyList())
                }
        } else {
            Log.d("StepCounts", "No user logged in")
            callback(false, emptyList())
        }
    }


    private fun fetchAndCountWorkouts() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("users").document(userId)
            .collection("completedWorkouts")
            .get()
            .addOnSuccessListener { documents ->
                val workouts = documents.mapNotNull { documentSnapshot ->
                    val workoutMap = documentSnapshot.get("workout") as? Map<*, *>
                    workoutMap?.get("name") as? String
                }
                updateListView(workouts)
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception)
            }
    }

    private fun updateListView(workouts: List<String>) {
        val workoutsCount = countWorkoutsByName(workouts)
        val allWorkoutTypes = listOf("Full Body Workout", "Pull Workout", "Push Workout", "Leg Workout", "Ab Workout")
        val displayList = allWorkoutTypes.map { workoutType ->
            "$workoutType - ${workoutsCount[workoutType] ?: 0} times"
        }
        displayList.forEach { item ->
            Log.d("ListViewContent", item)
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, displayList)
        listView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchAndCountWorkouts()
        fetchStepCounts { success, stepCounts ->
            if (success) {
                stepCountAdapter = StepCountAdapter(stepCounts)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = stepCountAdapter
            } else {
                //error handling
            }
        }
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyProgressViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

