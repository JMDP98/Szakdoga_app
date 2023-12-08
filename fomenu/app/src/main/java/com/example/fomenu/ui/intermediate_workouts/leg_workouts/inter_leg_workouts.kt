package com.example.fomenu.ui.intermediate_workouts.leg_workouts

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fomenu.MyRecycleViewAdapter
import com.example.fomenu.OnItemClickListener
import com.example.fomenu.R
import com.example.fomenu.workout_preview_activity

class inter_leg_workouts : Fragment(), OnItemClickListener{

    companion object {
        fun newInstance() = inter_leg_workouts()
    }

    private lateinit var viewModel: InterLegWorkoutsViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_beginner_ab_workouts, container, false)
        recyclerView = view.findViewById(R.id.myRV)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = MyRecycleViewAdapter(this,15,"Leg Workout")
        recyclerView.adapter = adapter

        return view
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(requireContext(), workout_preview_activity::class.java)

        intent.putExtra("position", position)
        intent.putExtra("itemText", "Leg I")


        startActivity(intent)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InterLegWorkoutsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}