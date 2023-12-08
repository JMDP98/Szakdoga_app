package com.example.fomenu.question

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fomenu.R
import com.google.android.material.snackbar.Snackbar

class question3_2 : AppCompatActivity(), MyRecycleViewAdapter.OnItemClickListener {

    val questionList = listOf<String>(
        "Gain Muscle",
        "Get Skills",
        "General Fitness",
        "Gain strenght",
        "Improve endurance",
        "Gain Muscle and get skills",
        "Gain muscle and endurance"
    )

    override fun onItemClick(position: Int, item: String) {
        val userProfile = intent.getParcelableExtra<UserProfile>("userProfile")
        Log.d("ItemClick", "onItemClick called with position: $position, item: $item")
        userProfile?.fitness_goal = item
        Log.d("ItemClick", "fitness_goal updated: ${userProfile?.fitness_goal}")

    }

    override fun onBackPressed() {
        // Do nothing to block the back press
    }

    private lateinit var adapter: MyRecycleViewAdapter // Declare the adapter as a property

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question32)
        val recycleView = findViewById<RecyclerView>(R.id.myRV)
        val btnNext = findViewById<Button>(R.id.btnNextQuestion)
        val userProfile = intent.getParcelableExtra<UserProfile>("userProfile")

        if(userProfile != null){
            Log.d("UserProfile", "Data received successfully: $userProfile")
        } else {
            Log.e("UserProfile", "Data not received")
        }

        recycleView.setBackgroundColor(Color.TRANSPARENT)
        recycleView.layoutManager = LinearLayoutManager(this)
        adapter = MyRecycleViewAdapter(questionList)
        adapter.onItemClickListener = this // Assuming your activity implements OnItemClickListener
        recycleView.adapter = adapter
        btnNext.setOnClickListener {

            if (adapter.isCardSelected()) {
                val intent: Intent
                val position = adapter.selectedPosition1
                val itemText = questionList[position]

                /*if (itemText == "Get Skills" || itemText == "Gain Muscle and Get Skills") {
                    userProfile?.fitness_goal = itemText
                    Log.d("UserProfile", "fitness_goal updated: ${userProfile?.fitness_goal}")
                    intent = Intent(this, question4::class.java)
                } else {*/
                    userProfile?.fitness_goal = itemText
                    Log.d("UserProfile", "fitness_goal updated: ${userProfile?.fitness_goal}")
                    intent = Intent(this, question5::class.java)

                intent.putExtra("userProfile", userProfile)
                startActivity(intent)
                finish()
            }
            else{
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView, "Please select one of the above!", Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    // You can add any action you want here, or leave it empty
                }
                // Apply negative margin to push Snackbar up
                val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
                params.setMargins(0, 0, 0, resources.getDimensionPixelOffset(R.dimen.snackbar_margin_bottom))
                snackbar.view.layoutParams = params
                snackbar.show()

                false
            }
        }

    }


}


