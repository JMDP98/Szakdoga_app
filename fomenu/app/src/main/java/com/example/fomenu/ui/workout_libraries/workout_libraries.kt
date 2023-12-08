package com.example.fomenu.ui.workout_libraries

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fomenu.MyAdapter
import com.example.fomenu.MyData
import com.example.fomenu.R
import com.example.fomenu.ui.your_workouts.your_workouts

class workout_libraries : Fragment() {

    companion object {
        fun newInstance() = workout_libraries()
    }

    private lateinit var viewModel: WorkoutLibrariesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var workoutLibBtn : Button
    private lateinit var yourWorkoutsBtn : Button
    private lateinit var tutorialsBtn : Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_workout_libraries, container, false)

        val dataList = listOf(
            MyData("Beginner Workouts",R.drawable.incline_pushup),
            MyData("Intermediate Workouts",R.drawable.l_sit_pullup),
            MyData("Advanced Workouts",R.drawable.oapu),

        )


        recyclerView = view.findViewById(R.id.recyclerView)


        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MyAdapter(dataList)


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

        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WorkoutLibrariesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}