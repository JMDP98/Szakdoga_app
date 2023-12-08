package com.example.fomenu.ui.intermediate_workouts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.setViewTreeFullyDrawnReporterOwner
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.fomenu.R
import com.example.fomenu.ui.your_workouts.your_workouts

class Intermediate_workouts : Fragment() {

    companion object {
        fun newInstance() = Intermediate_workouts()
    }

    private lateinit var viewModel: IntermediateWorkoutsViewModel
    private lateinit var workoutLibBtn : Button
    private lateinit var yourWorkoutsBtn : Button
    private lateinit var tutorialsBtn : Button
    private lateinit var fullBodyCV : CardView
    private lateinit var absCV : CardView
    private lateinit var legsCV : CardView
    private lateinit var pushCV : CardView
    private lateinit var pullCV : CardView
    private lateinit var backgroundImageView_PULL: ImageView
    private lateinit var backgroundImageView_ABS: ImageView
    private lateinit var backgroundImageView_PUSH: ImageView
    private lateinit var backgroundImageView_LEG: ImageView
    private lateinit var backgroundImageView_FULL_BODY: ImageView




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
        backgroundImageView_PULL = view.findViewById(R.id.backgroundImageView_PULL)
        backgroundImageView_ABS = view.findViewById(R.id.backgroundImageView_ABS)
        backgroundImageView_PUSH = view.findViewById(R.id.backgroundImageView_PUSH)
        backgroundImageView_LEG = view.findViewById(R.id.backgroundImageView_LEGS)
        backgroundImageView_FULL_BODY = view.findViewById(R.id.backgroundImageView_FB)





        backgroundImageView_PULL.setImageResource(R.drawable.archer_pullup)
        backgroundImageView_ABS.setImageResource(R.drawable.leg_raise)
        backgroundImageView_PUSH.setImageResource(R.drawable.clap_pushup)
        backgroundImageView_LEG.setImageResource(R.drawable.side_lunges)
        backgroundImageView_FULL_BODY.setImageResource(R.drawable.negative_pushup)




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
            navController.navigate(R.id.navigation_full_body_inter_Workouts)
        }
        pullCV.setOnClickListener {

            val navController = findNavController()
            // Navigate to the WorkoutLibraries fragment
            navController.navigate(R.id.navigation_pull_inter_Workouts)
        }
        pushCV.setOnClickListener {

            val navController = findNavController()
            // Navigate to the WorkoutLibraries fragment
            navController.navigate(R.id.navigation_push_inter_Workouts)
        }
        absCV.setOnClickListener {

            val navController = findNavController()
            // Navigate to the WorkoutLibraries fragment
            navController.navigate(R.id.navigation_ab_inter_Workouts)
        }
        legsCV.setOnClickListener {

            val navController = findNavController()
            // Navigate to the WorkoutLibraries fragment
            navController.navigate(R.id.navigation_leg_inter_Workouts)
        }

        return view    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(IntermediateWorkoutsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}