package com.example.fomenu

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

class MyWorkoutAdapter(private val workout: Workout,private val fragmentManager: FragmentManager): RecyclerView.Adapter<MyWorkoutHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyWorkoutHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.workout_prview_activit_rv_list_item,parent,false)
        return MyWorkoutHolder(listItem)

    }

    override fun onBindViewHolder(holder: MyWorkoutHolder, position: Int) {
        val exercise = workout.exercises[position]
        if (exercise != null) {
            holder.bind(exercise)
            holder.itemView.setOnClickListener {
                val videoUrl = exercise.videoPath
                preloadVideoUrl(videoUrl)
                val bottomSheetFragment = VideoBottomSheetFragment()
                bottomSheetFragment.setExercise(exercise)
                bottomSheetFragment.show(fragmentManager, "VideoBottomSheetFragment")
            }
        }
    }
    private fun preloadVideoUrl(videoUrl: String) {
        // Load video URL in the background
        // You can use a background thread, coroutine, or AsyncTask for this task
    }

    override fun getItemCount(): Int {
        return workout.exercises.size
    }


}

class MyWorkoutHolder(val view: View): RecyclerView.ViewHolder(view) {

    private val nameText: TextView = itemView.findViewById(R.id.nameTV)
    private val repText: TextView = itemView.findViewById(R.id.repTV)
    private val image: ImageView = itemView.findViewById(R.id.exerciseImage)
    private var currentExercise: Exercise = Exercise("", "", "", "")


    fun bind(exercise: Exercise) {
        currentExercise = exercise
        nameText.text = formatTextWithLineBreaks(exercise.name,20)
        repText.text = "Reps: ${exercise.repetitionCount}"
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageRef = storageRef.child(exercise.picturePath)

        imageRef.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()
            Log.d("ImageURL", imageUrl)
            Glide.with(view.context)
                .load(imageUrl)
                .override(100, 100)
                .into(image)
        }.addOnFailureListener {exception ->
            Log.e("ImageLoadError", "Error loading image: ${exception.message}", exception)
        }
    }
    private fun formatTextWithLineBreaks(text: String, maxLineLength: Int): String {
        val words = text.split(" ")
        val result = StringBuilder()
        var currentLineLength = 0

        for (word in words) {
            // If adding this word exceeds maxLineLength, insert a newline (unless it's the start of a line)
            if (currentLineLength + word.length > maxLineLength && currentLineLength > 0) {
                result.append("\n")
                currentLineLength = 0
            }

            // Add space before the word (but not at the start of a new line)
            if (currentLineLength > 0) {
                result.append(" ")
                currentLineLength++
            }

            result.append(word)
            currentLineLength += word.length
        }

        return result.toString()
    }
}
