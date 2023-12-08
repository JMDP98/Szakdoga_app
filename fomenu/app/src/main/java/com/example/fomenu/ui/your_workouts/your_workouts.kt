package com.example.fomenu.ui.your_workouts

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fomenu.CompletedWorkoutsRecycleViewAdapter
import com.example.fomenu.R
import com.example.fomenu.Workout
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class your_workouts : Fragment() {

    companion object {
        fun newInstance(completedWorkouts: List<Workout>, completionDates : List<String>): your_workouts {
            val fragment = your_workouts()
            val args = Bundle()
            args.putParcelableArrayList("completedWorkouts", ArrayList(completedWorkouts))
            args.putStringArrayList("completionDates", ArrayList(completionDates))
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: YourWorkoutsViewModel

    private lateinit var workoutLibBtn : Button
    private lateinit var yourWorkoutsBtn : Button
    private lateinit var tutorialsBtn : Button

    private val sharedPrefsKey = "completed_workouts"
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CompletedWorkoutsRecycleViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_your_workouts, container, false)

        workoutLibBtn = view.findViewById(R.id.button2)
        yourWorkoutsBtn = view.findViewById(R.id.button)
        tutorialsBtn = view.findViewById(R.id.button3)

        recyclerView = view.findViewById(R.id.myRV)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CompletedWorkoutsRecycleViewAdapter(requireContext())
        recyclerView.adapter = adapter

        workoutLibBtn.setOnClickListener {

            val navController = findNavController()
            // Navigate to the WorkoutLibraries fragment
            navController.navigate(R.id.navigation_workout_libraries)
        }
        yourWorkoutsBtn.setOnClickListener {

            val navController = findNavController()
            navController.navigate(R.id.navigation_your_workouts)
        }
        tutorialsBtn.setOnClickListener {

            val navController = findNavController()
            // Navigate to the WorkoutLibraries fragment
            navController.navigate(R.id.navigation_tutorials)
        }

        return view    }

    override fun onResume() {
        super.onResume()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(YourWorkoutsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}