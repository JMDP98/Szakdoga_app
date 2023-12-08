package com.example.fomenu.question

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fomenu.R

class MyRecycleViewAdapter(private val questionList: List<String>,private val allowMultipleSelection: Boolean = false) : RecyclerView.Adapter<MyRecycleViewAdapter.MyViewHolder>() {

    var selectedPosition1: Int = RecyclerView.NO_POSITION
    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: String)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item, parent, false)
        return MyViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val qq = questionList[position]
        val isSelected = position == selectedPosition1
        holder.bind(qq, isSelected)
    }

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val myCardView = view.findViewById<CardView>(R.id.cvGainMuscleRv)
        private val myTextView = view.findViewById<TextView>(R.id.tvGainMuscleRv)

        init {

            view.setOnClickListener {
                val position = adapterPosition
                Log.d("Adapter", "Item clicked at position $position")
                if (position != RecyclerView.NO_POSITION) {
                    if(!allowMultipleSelection){
                        if (selectedPosition1 != RecyclerView.NO_POSITION) {
                            notifyItemChanged(selectedPosition1)
                            onItemClickListener?.onItemClick(position,questionList[position])
                        }
                        setSelectedPosition(position)
                    }
                    else{
                        toggleSelectedPosition(position)
                        onItemClickListener?.onItemClick(position,questionList[position])

                    }

                    //CardManager.toggleCardSelection(position)
                    notifyItemChanged(position)
                }
            }
        }

        fun bind(question: String, isSelected: Boolean) {
            myTextView.text = question
            val backgroundColor = if (isSelected)
                ContextCompat.getColor(view.context, R.color.btnQuestionPressedColor)
            else
                Color.argb(0x30, 0xFF, 0xFF, 0xFF)
            myCardView.setBackgroundColor(backgroundColor)

        }
    }

    private fun setSelectedPosition(position: Int) {
        val previousPosition = selectedPosition1
        selectedPosition1 = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(position)
    }

    private fun toggleSelectedPosition(position: Int) {
        if (selectedPosition1 == position) {
            selectedPosition1 = RecyclerView.NO_POSITION
        } else {
            selectedPosition1 = position
        }
    }

    fun isCardSelected(): Boolean {
        return selectedPosition1 != RecyclerView.NO_POSITION
    }
    fun getSelectedItems(): List<String> {
        val selectedItems = mutableListOf<String>()
        for (i in questionList.indices) {
            if (selectedPosition1 == i) {
                selectedItems.add(questionList[i])
            }
        }
        return selectedItems
    }

}



