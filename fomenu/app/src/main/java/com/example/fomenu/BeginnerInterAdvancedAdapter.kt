package com.example.fomenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val dataList: List<MyData>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val backgroundImage: ImageView = itemView.findViewById(R.id.backgroundImageView)
        val tvBeginner : TextView = itemView.findViewById(R.id.tvBeginner)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_for_beginner_inter_advanced, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]
        //holder.itemView.tvBeginner.text = data.text
        holder.backgroundImage.setImageResource(data.backgroundImage)
        holder.tvBeginner.text = data.text

        holder.itemView.setOnClickListener {
            val navController = Navigation.findNavController(holder.itemView)
            val action = when (position) {
                0 -> navController.navigate(R.id.navigation_beginner_workouts)
                1 -> navController.navigate(R.id.navigation_intermediate_workouts)
                2 -> navController.navigate(R.id.navigation_advanced_workouts)
                else -> null
            }
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
