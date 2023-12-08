package com.example.fomenu

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.fomenu.ui.your_workouts.your_workouts
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

data class CompletedWorkout(
    val workout: Workout, // Replace 'String' with your actual Workout class or data type
    val completionDate: String
)


class TheWorkout : AppCompatActivity() {
    private var currentPosition = 0
    private lateinit var exercises: List<Exercise>
    private var countdownTimer: CountDownTimer? = null

    private lateinit var videoView: VideoView
    private lateinit var exerciseNameTextView: TextView
    private lateinit var nextExerciseTextView: TextView
    private lateinit var nextExerciseImage: ImageView
    private lateinit var startButton: Button
    private lateinit var timeText: TextView
    private lateinit var timerText: TextView
    private lateinit var completeEx: Button
    private lateinit var skipRest: Button
    private lateinit var repText: TextView
    private lateinit var nextRepText: TextView
    private val scope = CoroutineScope(Dispatchers.IO)
    private var isStarted = false
    private var time = 0.0
    private var stopwatchTime = 0.0
    private lateinit var serviceIntent: Intent
    private var roundsCompleted = 0
    private var totalRounds = 3
    private val sharedPrefsKey = "completed_workouts"
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var youTubePlayer: YouTubePlayer
    private var isPlayerInitialized = false
    private var completedWorkouts = mutableListOf<Workout>()
    private var completionDates = mutableListOf<String>()
    private var isRestPeriod = false

    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_the_workout)
        sharedPrefs = getSharedPreferences(sharedPrefsKey, Context.MODE_PRIVATE)

        val workout = intent.getParcelableExtra<Workout>("workout")
        //totalRounds = workout.rounds

        val userId = user?.uid
        val completedWorkoutsJson = sharedPrefs.getString("completedWorkouts", "")
        val completionDatesJson = sharedPrefs.getString("completionDates", "")
        completedWorkouts = if (!completedWorkoutsJson.isNullOrEmpty()) {
            Gson().fromJson(completedWorkoutsJson, object : TypeToken<List<Workout>>() {}.type)
        } else {
            mutableListOf()
        }

        completionDates = if (!completionDatesJson.isNullOrEmpty()) {
            Gson().fromJson(completionDatesJson, object : TypeToken<List<String>>() {}.type)
        } else {
            mutableListOf()
        }

        Log.d("SharedPrefs", "Completed Workouts: $completedWorkouts")
        Log.d("SharedPrefs", "Completion Dates: $completionDates")

        Log.d("CompletedWprkouts", "After adding item to completedWorkouts $completedWorkouts Size: ${completedWorkouts.size}")
        Log.d("CompletedWprkouts", "Date: $completionDates")

        exerciseNameTextView = findViewById(R.id.exerciseNameTextView)
        nextExerciseTextView = findViewById(R.id.nameTV)
        nextExerciseImage = findViewById(R.id.exerciseImage)
        startButton = findViewById(R.id.btnStart)
        timeText = findViewById(R.id.tvTime)
        timerText = findViewById(R.id.tvTimer)
        completeEx = findViewById(R.id.completeEx)
        skipRest = findViewById(R.id.skipRestButton)
        repText = findViewById(R.id.repText)
        nextRepText = findViewById(R.id.nextRepText)
        val rest = workout?.rest ?: ""
        val type = workout?.type ?: ""
        totalRounds = workout?.rounds ?: 0

        startButton.setOnClickListener {
            startOrStop()
        }
        serviceIntent = Intent(applicationContext, StopWatchService::class.java)
        registerReceiver(updateTime, IntentFilter(StopWatchService.UPDATED_TIME))
        createCountdownTimer(type)
        retrieveFromSharedPreferences()

        if (workout != null) {
            exercises = workout.exercises
            val exercise = exercises[currentPosition]
            displayExercise(exercise)
        }
        val youTubePlayerView: YouTubePlayerView = findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
                isPlayerInitialized = true
                if (workout != null && workout.exercises.isNotEmpty()) {
                    val firstExercise = workout.exercises.first()
                    val firstVideoId = firstExercise.videoPath // Assuming videoPath is the video ID
                    youTubePlayer.setVolume(0)
                    youTubePlayer.loadVideo(firstVideoId, 0f)
                }
            }

            override fun onStateChange(player: YouTubePlayer, state: PlayerConstants.PlayerState) {
                super.onStateChange(player, state)
                if (state == PlayerConstants.PlayerState.ENDED) {
                    // Video has ended, replay it
                    player.seekTo(0f)
                    player.play()
                }
            }
        })

        completeEx.setOnClickListener {
            Log.d("TheWorkout1", "Exercise completed. Type: $type, currentPosition: $currentPosition, roundsCompleted: $roundsCompleted")
            if (type == "Normal" && roundsCompleted < totalRounds) {
                createCountdownTimer(type)
                roundsCompleted++

                if (roundsCompleted == totalRounds) {
                    if (currentPosition < exercises.size - 1) {
                        currentPosition++
                        countdownTimer?.cancel()
                        // Create a new timer with 180000 milliseconds
                        createCountdownTimer(type)

                        val nextExercise = exercises[currentPosition]
                        displayExercise(nextExercise)
                        roundsCompleted = 0
                    }
                    if(currentPosition == exercises.size-1 && roundsCompleted == totalRounds-1){

                        stop()
                        //stopwatch értéket adni
                        if (workout != null){
                            val fragment = your_workouts()
                            showCongratulationsDialog(workout, fragment)
                        }
                    }
                }
                else {

                }
                countdownTimer?.start()
                completeEx.visibility = View.INVISIBLE
                skipRest.visibility = View.VISIBLE
            }
            else if (type == "Circuit"){
                countdownTimer?.start()
                completeEx.visibility = View.INVISIBLE
                skipRest.visibility = View.VISIBLE

                if (!isRestPeriod) {
                    if (currentPosition < exercises.size - 1) {
                        currentPosition++
                    } else {
                        if (roundsCompleted < totalRounds - 1) {
                            // End of round, start 3-minute rest
                            isRestPeriod = true
                            currentPosition = 0
                            roundsCompleted++
                            countdownTimer?.cancel()
                            createCountdownTimer(type)
                            countdownTimer?.start()
                        } else {
                            // Final round completed
                            stop()
                            if (workout != null) {
                                val fragment = your_workouts()
                                showCongratulationsDialog(workout, fragment)
                            }
                        }
                    }
                } else {
                    // End of rest period, start new round
                    isRestPeriod = false
                    currentPosition = 0
                    roundsCompleted++
                    countdownTimer?.cancel()
                    createCountdownTimer(type)
                    countdownTimer?.start()
                }

                val nextExercise = exercises[currentPosition]
                displayExercise(nextExercise)
            }

                if (currentPosition == exercises.size - 1 && roundsCompleted == totalRounds-1) {
                    stop()
                    if (workout != null) {
                        val fragment = your_workouts()
                        showCongratulationsDialog(workout, fragment)
                    }
                }
            }

        skipRest.setOnClickListener {
            // Set the timer value to zero
            Log.d("TheWorkout1", "Skipping rest. Before reset, isRestPeriod: $isRestPeriod")

            countdownTimer?.cancel()
            timerText.text = "00:00"

            // Hide Skip Rest Button
            skipRest.visibility = View.GONE

            // Show Complete Exercise Button
            completeEx.visibility = View.VISIBLE

            if (isRestPeriod && type == "Circuit") {
                isRestPeriod = false
                countdownTimer?.cancel()
                createCountdownTimer(type)
                Log.d("TheWorkout1", "Rest period skipped. Resetting isRestPeriod to false. $isRestPeriod")

            }
        }

    }
    fun createCountdownTimer(type: String) {
        Log.d("TheWorkout1", "Creating countdown timer. Type: $type, isRestPeriod: $isRestPeriod")

        val timerDuration = when {
            isRestPeriod -> 180000L // 3 minutes for rest
            type == "Normal" -> 90000L // 90 seconds
            else -> 60000L // 60 seconds for "Circuit"
        }
        countdownTimer = object : CountDownTimer(timerDuration,1000
        )
        {
            override fun onTick(millisUntilFinished: Long) {
                // Update the timer text on each tick
                val formattedTime = getFormattedTime2(millisUntilFinished)
                timerText.text = formattedTime

                if (formattedTime == "00:00") {
                    // Trigger the desired actions when timer reaches 00:00
                    skipRest.performClick() // Programmatically trigger skipRest button click
                }
            }

            override fun onFinish() {
                //javítani
                Log.d("TheWorkout1", "Timer finished. isRestPeriod: $isRestPeriod, currentPosition: $currentPosition, roundsCompleted: $roundsCompleted")
                timerText.text = "00:00"
                completeEx.visibility = View.VISIBLE
                skipRest.visibility = View.INVISIBLE
                if (isRestPeriod && type == "Circuit") {
                    isRestPeriod = false
                    countdownTimer?.cancel()
                    createCountdownTimer(type)
                    Log.d("TheWorkout1", "Rest period ended. Resetting isRestPeriod to false. $isRestPeriod")

                }

                if (currentPosition < exercises.size - 1) {
                    currentPosition++
                    val nextExercise = exercises[currentPosition]
                    displayExercise(nextExercise)
                    countdownTimer?.start() // Start the timer for the next exercise
                } else {

                }
            }
        }
        Log.d("TheWorkout1", "Countdown timer created with duration: $timerDuration")

    }

    override fun onBackPressed() {
        stop() // Stop the stopwatch (this will also reset it)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Discard Workout")
        builder.setMessage("Are you sure you want to discard this workout?")

        builder.setPositiveButton("Continue") { dialog, which ->
            // Continue with the workout (do nothing)
            dialog.dismiss()
        }
        builder.setNegativeButton("Discard") { dialog, which ->
            // Perform actions to discard the workout
            // For example, navigate to a different activity or finish the current activity
            finish() // This will finish the current activity and go back
        }

        val dialog = builder.create()
        dialog.show()
    }
    private fun CompletedWorkout(workout : Workout){
        completedWorkouts.add(workout)
    }
    private fun CompletionDate(completionDate: String) {
        completionDates.add(completionDate)
    }
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
    private fun showCongratulationsDialog(workout: Workout, fragment: your_workouts) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Congratulations")
        builder.setMessage("You have completed the workout!")

        builder.setPositiveButton("OK") { dialog, which ->
            Log.d("CompletedWprkouts", "Before adding item to completedWorkouts $completedWorkouts")
            val currentDate = getCurrentDate()
            user?.let { firebaseUser ->
                // Directly create a map of data or a CompletedWorkout object to store in Firestore
                val workoutData = mapOf(
                    "workout" to workout, // Replace with actual workout data
                    "completionDate" to currentDate // Replace with actual completion date
                )

                // Define the document reference within the user's specific subcollection
                val workoutDocumentRef = db.collection("users").document(firebaseUser.uid)
                    .collection("completedWorkouts").document()

                // Write the workout data to Firestore
                workoutDocumentRef.set(workoutData)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Workout for user ${firebaseUser.uid} successfully written!")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error writing document for user ${firebaseUser.uid}", e)
                    }

                // Optionally, you can also add the workout to a list in memory if needed for the current session
                // completedWorkouts.add(workout)
                // completionDates.add(completionDate)
            } ?: run {
                Log.w("Firestore", "No authenticated user found. Cannot write workouts to Firestore.")
            }
            Log.d("CompletedWprkouts", "After adding item to completedWorkouts $completedWorkouts Size: ${completedWorkouts.size}")
            Log.d("CompletedWprkouts", "Date: $completionDates")
            finish()
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
    private fun saveToSharedPreferences() {
        val editor = sharedPrefs.edit()
        editor.putString("completedWorkouts", Gson().toJson(completedWorkouts))
        editor.putString("completionDates", Gson().toJson(completionDates))
        editor.apply()
    }
    private fun retrieveFromSharedPreferences() {
        val completedWorkoutsJson = sharedPrefs.getString("completedWorkouts", "")
        val completionDatesJson = sharedPrefs.getString("completionDates", "")

        completedWorkouts = Gson().fromJson(completedWorkoutsJson, object : TypeToken<List<Workout>>() {}.type) ?: mutableListOf()
        completionDates = Gson().fromJson(completionDatesJson, object : TypeToken<List<String>>() {}.type) ?: mutableListOf()
    }

    private fun startOrStop(){
        if(isStarted){
            stop()
        }
        else{
            start()
        }
    }
    private fun start(){
        serviceIntent.putExtra(StopWatchService.CURRENT_TIME,time)
        startService(serviceIntent)
        startButton.text = "STOP"
        isStarted = true
    }
    private fun stop(){
        stopService(serviceIntent)
        startButton.text = "START"
        isStarted = false
    }
    private val updateTime : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context : Context, intent : Intent) {
            time = intent.getDoubleExtra(StopWatchService.CURRENT_TIME,0.0)

            timeText.text = getFormattedTime(time)
            if (!isStarted) {
                val timerValue = "Timer: ${getFormattedTime(stopwatchTime)}"
                timerText.text = timerValue
            }

        }

    }
    private fun getFormattedTime(time:Double):String{
        val timeInt = time.roundToInt()
        val hours = timeInt % 86400 / 3600
        val minutes = timeInt % 86400 % 3600 / 60
        val seconds = timeInt % 86400 % 3600 % 60

        return String.format("%02d:%02d:%02d",hours,minutes,seconds)


    }
    private fun getFormattedTime2(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
    private suspend fun fetchVideoIdAsync(exercise: Exercise): String {
        return exercise.videoPath
    }
    private fun loadVideo(videoId: String) {
        if (isPlayerInitialized) {
            youTubePlayer.setVolume(0)
            youTubePlayer.loadVideo(videoId, 0f)
            youTubePlayer.play()
        }
    }
    private fun displayExercise(exercise: Exercise) {

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        if (::youTubePlayer.isInitialized) {
            val videoId = exercise.videoPath
            youTubePlayer.setVolume(0)
            loadVideo(videoId)
        } else {
            // Optionally handle the case where the player is not yet initialized
            Log.d("TheWorkout", "YouTubePlayer is not initialized yet")
        }

        /*scope.launch {
            val videoId = fetchVideoIdAsync(exercise)
            withContext(Dispatchers.Main) {
                youTubePlayer.setVolume(0)
                loadVideo(videoId)

            }
            }

         */

        Log.d("VideoID", "${exercise.videoPath}")

        exerciseNameTextView.text = exercise.name
        repText.text = "Reps: ${exercise.repetitionCount}"

        // Update next exercise details

        if (currentPosition < exercises.size - 1) {
            val nextExercise = exercises[currentPosition + 1]
            nextExerciseTextView.text = "Name: ${nextExercise.name}"
            nextRepText.text = "Reps: ${nextExercise.repetitionCount}"
            val imageRef = storageRef.child(nextExercise.picturePath)

            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                // Load the image into nextExerciseImage using Glide
                Glide.with(this).load(imageUrl).into(nextExerciseImage)
            }.addOnFailureListener { exception ->
                // Handle any errors here
            }
        }
        else {
            nextExerciseTextView.text = "End of Exercises"
            nextRepText.text = ""
            nextExerciseImage.setImageResource(R.drawable.well_done)

        }
    }

}