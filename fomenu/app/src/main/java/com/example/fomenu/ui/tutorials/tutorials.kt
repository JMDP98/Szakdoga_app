package com.example.fomenu.ui.tutorials

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.fomenu.R
import com.example.fomenu.ui.your_workouts.your_workouts

class tutorials : Fragment() {

    companion object {
        fun newInstance() = tutorials()
    }

    private lateinit var viewModel: TutorialsViewModel
    private lateinit var workoutLibBtn : Button
    private lateinit var yourWorkoutsBtn : Button
    private lateinit var tutorialsBtn : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_tutorials, container, false)


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
            // Navigate to the WorkoutLibraries fragment
            navController.navigate(R.id.navigation_tutorials)
        }


        return view    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TutorialsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}