package com.example.fomenu


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StatisticsAdapter(private val context: Context) : RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.statistics_recycle_view_item, parent, false)
        return StatisticsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        // Set the text for TextViews
        holder.itemView.findViewById<TextView>(R.id.numberOfWorkoutsCompleted)?.text = "Number of Workouts Completed: 5"
        holder.itemView.findViewById<TextView>(R.id.dailyWorkoutsCompleted)?.text = "Daily Workouts Completed: 3"
        holder.itemView.findViewById<TextView>(R.id.loginsCount)?.text = "Logins: 10"

        // Assuming you have a list of workouts, here's an example list
        val workouts = listOf("Workout 1", "Workout 2", "Workout 3", "Workout 4", "Workout 5","HEHEHEHE","Hehehehehe","Hehehehehe","hehehe","hehehe")

        // Set the ListView adapter with a maximum of 5 items
        val adapter =
            ArrayAdapter(context, android.R.layout.simple_list_item_1, workouts.subList(0, 10))
        holder.itemView.findViewById<ListView>(R.id.completedWorkoutsList)?.adapter = adapter

        // Set an OnClickListener for the diagram (ImageView)
        holder.itemView.findViewById<ImageView>(R.id.diagram)?.setOnClickListener {
            // Do something when the diagram is clicked
        }
    }

    override fun getItemCount(): Int {
        return 1 // Only one item in the RecyclerView
    }


inner class StatisticsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}


