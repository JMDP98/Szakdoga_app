package com.example.fomenu

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fomenu.ui.home.HomeViewModel

class StepCountReceiver(private val viewModel: HomeViewModel) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val steps = intent.getIntExtra("stepCount", 0)
        viewModel.updateStepCount(steps)
    }
}