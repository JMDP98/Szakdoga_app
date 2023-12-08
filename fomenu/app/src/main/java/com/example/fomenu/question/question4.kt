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
import com.example.fomenu.question.UserProfile
import com.example.fomenu.question.MyRecycleViewAdapter
import com.example.fomenu.R
import com.google.android.material.snackbar.Snackbar

class question4 : AppCompatActivity(), MyRecycleViewAdapter.OnItemClickListener  {

    val questionList = listOf<String>(
        "Front lever",
        "Planche",
        "Handstand",
        "L-sit",
        "One Arm Pull-up",
        "Handstand Push-up",
        "Human Flag",
        "asd"
    )

    private lateinit var adapter: MyRecycleViewAdapter

    override fun onItemClick(position: Int, item: String) {
        Log.d("ItemClicked", "Clicked item: $item")
        val userProfile = intent.getParcelableExtra<UserProfile>("userProfile")
        Log.d("UserProfile", "User profile: $userProfile")
        val position = adapter.selectedPosition1
        // Get the item at the specified position
        val itemText = questionList[position]
        userProfile?.skills = userProfile?.skills.orEmpty().toMutableList().apply {
            if(contains(itemText)){
                remove(itemText)
            }
            else{
                add(itemText)
            }
        }
        //userProfile?.skills= adapter.getSelectedItems()
        Log.d("UserProfile", "Skills after modification: ${userProfile?.skills}")

    }

    override fun onBackPressed() {
        // Do nothing to block the back press
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question4)
        val recycleView = findViewById<RecyclerView>(R.id.myRVq4)
        val btnNext = findViewById<Button>(R.id.btnNextQuestionq4)
        val userProfile = intent.getParcelableExtra<UserProfile>("userProfile")


        if(userProfile != null){
            Log.d("UserProfile", "Data received successfully: $userProfile")
        } else {
            Log.e("UserProfile", "Data not received")
        }

        recycleView.setBackgroundColor(Color.TRANSPARENT)
        recycleView.layoutManager = LinearLayoutManager(this)
        adapter = MyRecycleViewAdapter(questionList,allowMultipleSelection = true)
        adapter.onItemClickListener = this
        recycleView.adapter = adapter

        btnNext.setOnClickListener {

            if (userProfile != null) {
                Log.d("UserProfile", "Data received successfully: $userProfile")
                Log.d("UserProfile", "Skills before moving to next activity: ${userProfile.skills}")

                // Now, userProfile.skills will contain the selected skills
            } else {
                Log.e("UserProfile", "Data not received")
            }

            if (adapter.isCardSelected()) {
                val intent = Intent(this, question5::class.java)
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