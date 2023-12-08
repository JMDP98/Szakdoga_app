package com.example.fomenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fomenu.OnItemClickListener
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class MyRecycleViewAdapter(private val listener: OnItemClickListener,private val itemCount: Int, private val itemText: String): RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.workout_frg_rv_list_item,parent,false)
        return MyViewHolder(listItem)

    }

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var new_pos = position+1
        holder.myTextView.text = "$itemText $new_pos"
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }

    }


}

class MyViewHolder(val view:View):RecyclerView.ViewHolder(view) {
    val myCardView: CardView = itemView.findViewById(R.id.cvFull_body_item)
    val myTextView = view.findViewById<TextView>(R.id.tvFull_body_list_item)

    init {
        view.setOnClickListener {

        }
    }

}

