package com.example.fomenu.ui.workout

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fomenu.MyRecycleViewAdapter
import com.example.fomenu.OnItemClickListener
import com.example.fomenu.R

import com.example.fomenu.ui.workout_libraries.workout_libraries
import com.example.fomenu.ui.your_workouts.your_workouts
import com.example.fomenu.workout_preview_activity

class Workout : Fragment(),OnItemClickListener {

    companion object {
        fun newInstance() = workout_libraries()
    }

    private lateinit var viewModel: WorkoutViewModel
    private lateinit var workoutLibBtn : Button
    private lateinit var yourWorkoutsBtn : Button
    private lateinit var tutorialsBtn : Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        val view =  inflater.inflate(R.layout.fragment_workout, container, false)

        workoutLibBtn = view.findViewById(R.id.button2)
        yourWorkoutsBtn = view.findViewById(R.id.button)
        tutorialsBtn = view.findViewById(R.id.button3)


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
            navController.navigate(R.id.navigation_tutorials)
        }

        return view
    }

    override fun onItemClick(position: Int) {
        // Create an Intent to start the activity where you want to display the workout
        val intent = Intent(requireContext(), workout_preview_activity::class.java)

        intent.putExtra("position", position)

        startActivity(intent)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)
        // TODO: Use the ViewModel
    }

}