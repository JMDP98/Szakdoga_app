package com.example.fomenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.VideoView
import com.google.firebase.storage.FirebaseStorage

class VideoActivity : AppCompatActivity() {
    private var y1: Float = 0f
    private var y2: Float = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)


        val exercise = intent.getParcelableExtra<Exercise>("exercise") as Exercise
        // Retrieve data from intent extras
        val videoUrl = intent.getStringExtra("video_url")
        val videoView = findViewById<VideoView>(R.id.exerciseVideoView)
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        if (exercise != null) {
            val videoRef = storageRef.child(exercise.videoPath) // Replace with actual path

            videoRef.downloadUrl.addOnSuccessListener { uri ->
                val videoUrl = uri.toString()
                videoView.setVideoPath(videoUrl)
                videoView.start()
            }.addOnFailureListener { exception ->
                // Handle any errors here
            }
        }
        else{

        }
        Log.e("VideoActivity", "Exercise object is null")
    }

    // Detect swipe gesture

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> y1 = event.y
            MotionEvent.ACTION_UP -> {
                y2 = event.y
                val deltaY = y2 - y1
                if (deltaY > MIN_SWIPE_DISTANCE) {
                    // Downward swipe detected, finish the activity
                    finish()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    companion object {
        private const val MIN_SWIPE_DISTANCE = 150
    }
}
