package com.example.fomenu.ui.beginner_workouts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.fomenu.R
import com.example.fomenu.ui.your_workouts.your_workouts

class beginner_workouts : Fragment() {

    companion object {
        fun newInstance() = beginner_workouts()
    }

    private lateinit var viewModel: BeginnerWorkoutsViewModel

    private lateinit var workoutLibBtn : Button
    private lateinit var yourWorkoutsBtn : Button
    private lateinit var tutorialsBtn : Button
    private lateinit var fullBodyCV : CardView
    private lateinit var absCV : CardView
    private lateinit var legsCV : CardView
    private lateinit var pushCV : CardView
    private lateinit var pullCV : CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_beginner_workouts, container, false)


        workoutLibBtn = view.findViewById(R.id.button2)
        yourWorkoutsBtn = view.findViewById(R.id.button)
        tutorialsBtn = view.findViewById(R.id.button3)
        fullBodyCV = view.findViewById(R.id.cvFullBody)
        absCV = view.findViewById(R.id.cvAbs)
        legsCV = view.findViewById(R.id.cvLegs)
        pushCV = view.findViewById(R.id.cvPush)
        pullCV = view.findViewById(R.id.cvPull)


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

        fullBodyCV.setOnClickListener {

            val navController = findNavController()
            // Navigate to the WorkoutLibraries fragment
            navController.navigate(R.id.navigation_full_body_beginner_Workouts)
        }
        pullCV.setOnClickListener {

            val navController = findNavController()
            // Navigate to the WorkoutLibraries fragment
            navController.navigate(R.id.navigation_pull_beginner_Workouts)
        }
        pushCV.setOnClickListener {

            val navController = findNavController()
            // Navigate to the WorkoutLibraries fragment
            navController.navigate(R.id.navigation_push_beginner_Workouts)
        }
        absCV.setOnClickListener {

            val navController = findNavController()
            // Navigate to the WorkoutLibraries fragment
            navController.navigate(R.id.navigation_ab_beginner_Workouts)
        }
        legsCV.setOnClickListener {

            val navController = findNavController()
            // Navigate to the WorkoutLibraries fragment
            navController.navigate(R.id.navigation_leg_beginner_Workouts)
        }

        return view    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BeginnerWorkoutsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}