package com.example.fomenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fomenu.database.StepCountEntity
import com.example.fomenu.ui.my_progress.StepCount

class StepCountAdapter(private val stepCounts: List<StepCount>) : RecyclerView.Adapter<StepCountAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.textViewDate)
        val stepCountTextView: TextView = view.findViewById(R.id.textViewStepCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_step_count, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stepCount = stepCounts[position]
        holder.dateTextView.text = stepCount.date
        holder.stepCountTextView.text = stepCount.stepCount.toString()
    }

    override fun getItemCount() = stepCounts.size
}

