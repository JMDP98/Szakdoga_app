package com.example.fomenu.ui.my_progress.weight

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.db.williamchart.view.LineChartView
import com.example.fomenu.R
import com.example.fomenu.WeightAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class WeightClass(val weight: Double, val date: String)


class Weight : Fragment() {

    companion object {
        fun newInstance() = Weight()
        private const val SHARED_PREFS_LAST_DIALOG_DATE = "last_dialog_date"

    }

    private lateinit var viewModel: WeightViewModel
    private lateinit var lineChart: LineChartView
    private lateinit var textChartData : TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeightAdapter
    private val animationDuration = 1000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_weight, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        lineChart = view.findViewById(R.id.line_chart)
        textChartData = view.findViewById(R.id.tvChartData)
        lineChart.labelsFormatter = { value -> String.format("%.1f", value) }
        lineChart.isEnabled = false


        lineChart.gradientFillColors =
            intArrayOf(
                Color.parseColor("#81FFFFFF"),
                Color.TRANSPARENT
            )
        lineChart.animation.duration = animationDuration

        viewLifecycleOwner.lifecycleScope.launch {
            fetchWeightData()
        }
        showWeightDialog()

        return view
    }
    private fun checkTodaysEntryExists(callback: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val todayDate = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(Calendar.getInstance().time)

        currentUser?.let { user ->
            db.collection("users")
                .document(user.uid)
                .collection("weights")
                .whereEqualTo("date", todayDate)
                .whereEqualTo("flag", true)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.size() > 0) {
                        // Entry for today exists
                        callback(true)
                    } else {
                        // No entry for today
                        callback(false)
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error checking today's entry", e)
                    callback(false)
                }
        } ?: callback(false) // Handle the case where currentUser is null
    }


    private fun saveWeightToFirestore(weightClass: List<WeightClass>) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        Log.d("Firestore", "Current user: ${currentUser?.uid}")
        val db = FirebaseFirestore.getInstance()

        currentUser?.let {
            weightClass.forEach { weightEntry ->
                val weightData = hashMapOf(
                    "weight" to weightEntry.weight,
                    "date" to weightEntry.date,
                    "flag" to true
                )

                if (currentUser != null) {
                    val userId = currentUser.uid

                    // Your Firestore code here
                    FirebaseFirestore.getInstance().collection("users")
                        .document(userId).collection("weights")
                        .add(weightData)
                        .addOnSuccessListener { documentReference ->
                            Log.d(
                                "WeightFirebase",
                                "DocumentSnapshot added with ID: ${documentReference.id}"
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.w("WeightFirebase", "Error adding document", e)
                        }
                } else {
                    // Handle the case where there is no authenticated user
                }
            }
        }
    }

    private fun fetchWeightData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val weightEntries = mutableListOf<WeightClass>()

        currentUser?.let { user ->
            db.collection("users")
                .document(user.uid)
                .collection("weights")
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val weight = document.getDouble("weight")?.toFloat() ?: 0f
                        val date = document.getString("date") ?: ""
                        weightEntries.add(WeightClass(weight.toDouble(), date))
                    }
                    updateLineChart(weightEntries.map { Pair(it.date, it.weight.toFloat()) })
                    updateRecyclerView(weightEntries)
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error fetching weight data", e)
                }
        }
    }
    private fun updateRecyclerView(weightDataList: MutableList<WeightClass>) {
        if (::adapter.isInitialized) {
            adapter.weightList = weightDataList
            adapter.updateData(weightDataList)
        } else {
            adapter = WeightAdapter(weightDataList)
            recyclerView.adapter = adapter
        }
    }


    private fun updateLineChart(weightData: List<Pair<String, Float>>) {
        Log.d("UpdateLineChart", "Received data: $weightData")

        // Check if there is enough data
        if (weightData.size > 1) {
            // Create a new list with modified labels
            val modifiedData = weightData.mapIndexed { index, pair ->
                when (index) {
                    0 -> pair // First label
                    weightData.size - 1 -> pair // Last label
                    else -> "" to pair.second // Empty string for other labels
                }
            }

            // Animate the chart with the modified data
            lineChart.animate(modifiedData)
        } else if (weightData.isNotEmpty()) {
            // If there's only one data point, display it normally
            lineChart.animate(weightData)
        }

        if (weightData.isNotEmpty()) {
            textChartData.text = "Last weight entered: ${weightData.last().second}"
            textChartData.visibility = View.VISIBLE
        } else {
            textChartData.visibility = View.GONE
        }
        lineChart.isEnabled = true

    }
    private fun showWeightDialog(){
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        checkTodaysEntryExists { entryExists ->
            if (!entryExists) {
                val builder = AlertDialog.Builder(requireContext())
                val inflater = requireActivity().layoutInflater
                val dialogView = inflater.inflate(R.layout.popup_for_weight, null)
                val etWeight = dialogView.findViewById<EditText>(R.id.etWeight)

                builder.setView(dialogView)
                    .setPositiveButton("Save") { _, _ ->
                        val weight = etWeight.text.toString().toDoubleOrNull()
                        if (weight != null && weight > 0) {
                            // Convert the date to yy.MM.dd format
                            val formattedDate = SimpleDateFormat(
                                "yyyy.MM.dd",
                                Locale.getDefault()
                            ).format(Calendar.getInstance().time)
                            val weightClass = listOf(WeightClass(weight, formattedDate))
                            saveWeightToFirestore(weightClass)

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Invalid weight value",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .setNegativeButton("Cancel") { _, _ -> }
                    .setCancelable(false)
                    .show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeightViewModel::class.java)
        // TODO: Use the ViewModel
    }

}