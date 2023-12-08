package com.example.fomenu.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fomenu.R

class HomeAdapter(private val dataList: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Get the current item from dataList
        val currentItem = dataList[position]

        // Bind data to the views here
        val itemView = holder.itemView
        val buttonLogout = itemView.findViewById<Button>(R.id.buttonLogout)
        val textName = itemView.findViewById<TextView>(R.id.tvName)
        val calendarView = itemView.findViewById<CalendarView>(R.id.calendarView)

        // Set click listener for buttonLogout
        buttonLogout.setOnClickListener {
            // Your logout logic here
        }



        // Set text for textName
        //textName.text = currentItem.name
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
