package com.example.fomenu.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fomenu.question.UserProfile
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {

    private var _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _stepCount = MutableLiveData<Int>()
    val stepCount: LiveData<Int> = _stepCount

    fun updateStepCount(newCount: Int) {
        _stepCount.value = newCount
    }

    fun setUsername(username: String) {
        _username.value = username
    }

    val userProfile = MutableLiveData<UserProfile?>()

    fun fetchUserProfile(userId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("user_profiles").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val profile = documentSnapshot.toObject(UserProfile::class.java)
                    userProfile.value = profile
                    Log.d("HomeViewModel", "UserProfile fetched: $profile")

                } else {
                    userProfile.value = null
                    Log.d("HomeViewModel", "UserProfile not found")

                }
            }
            .addOnFailureListener {
                userProfile.value = null
                Log.e("HomeViewModel", "Error fetching UserProfile", it)

            }
    }
}