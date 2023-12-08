package com.example.fomenu

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CalendarView
import android.widget.TextView

class CustomCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CalendarView(context, attrs, defStyleAttr) {

    private val dateTextView: TextView = TextView(context)

    init {
        dateTextView.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        dateTextView.visibility = View.GONE
        addView(dateTextView)
    }

    fun setDateText(dateText: String) {
        dateTextView.text = dateText
        dateTextView.visibility = View.VISIBLE
    }

    fun hideDateText() {
        dateTextView.visibility = View.GONE
    }
}
