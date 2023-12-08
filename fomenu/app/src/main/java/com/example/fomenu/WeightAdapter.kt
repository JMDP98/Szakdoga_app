package com.example.fomenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fomenu.ui.my_progress.weight.WeightClass

class WeightAdapter(var weightList: MutableList<WeightClass>) : RecyclerView.Adapter<WeightAdapter.ViewHolder>() {

    fun updateData(newData: List<WeightClass>) {
        weightList.clear()
        weightList.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewDate: TextView = view.findViewById(R.id.textViewDate)
        val textViewStepCount: TextView = view.findViewById(R.id.textViewStepCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_step_count, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = weightList[position]
        holder.textViewDate.text = item.date
        val asd = String.format("%.1f", item.weight)
        holder.textViewStepCount.text = "$asd kg"
    }

    override fun getItemCount() = weightList.size
}
