package com.example.fomenu.ui.home

import PopupDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.fomenu.Exercise
import com.example.fomenu.databinding.FragmentHomeBinding
import com.example.fomenu.R
import com.example.fomenu.StepCountReceiver
import com.example.fomenu.Workout
import com.example.fomenu.WorkoutPreviewRecommended
import com.example.fomenu.generateWorkoutPlan
import com.example.fomenu.question.UserProfile
import com.google.android.gms.tasks.Tasks
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

data class UserName(
    val username: String,
    val isPopupShown: Boolean = false
) {
    constructor() : this("") {
        // This is the no-argument constructor
    }
}

class HomeFragment : Fragment(), PopupDialog.UsernameListener {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var logOutBtn : Button
    private lateinit var userText : TextView
    private lateinit var stepCount : TextView
    private var selectedDate: String? = null
    private lateinit var recommendedWorkoutTextView: TextView
    private var userProfile: UserProfile? = null
    private var completedWorkouts = mutableListOf<Workout>() // List to store workouts
    private lateinit var sharedPrefs: SharedPreferences
    private val sharedPrefsKey = "completed_workouts"
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var stepCountReceiver: BroadcastReceiver
    var dateToWorkoutMap = mutableMapOf<String,Workout>()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()
    private var recommendedWorkout: Workout? = null // Declare as a member variable


    var selectedWorkout: Workout? = null

    companion object {
        private const val SHARED_PREFS_NAME = "app_prefs"
        private const val KEY_POPUP_SHOWN = "popup_shown"
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        val sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val hasPopupBeenShown = sharedPreferences.getBoolean(KEY_POPUP_SHOWN, false)

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        userText = view.findViewById(R.id.user)
        stepCount = view.findViewById(R.id.stepCount)


        stepCountReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                // Handle the received broadcast (update UI, etc.)
                val steps = intent.getIntExtra("stepCount", 0)
                activity?.runOnUiThread {
                    stepCount.text = "Current daily step count: $steps"
                }
            }
        }
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val color = ContextCompat.getColor(requireContext(), R.color.calendarColor)
        calendarView.setBackgroundColor(color)
        val selectedDateTextView = view.findViewById<TextView>(R.id.selectedDateTextView)
        recommendedWorkoutTextView = view.findViewById(R.id.tvREC)

        fetchCompletedWorkouts { success, fetchedWorkouts ->
            if (success) {
                completedWorkouts.clear()
                completedWorkouts.addAll(fetchedWorkouts)
                Log.d("CompletedWorkouts", "Fetched ${completedWorkouts.size} workouts")
                completedWorkouts.forEach { workout ->
                    Log.d("CompletedWorkouts", "Workout: $workout")
                }
            } else {
                Log.d("CompletedWorkouts", "Error fetching workouts")
            }
        }




        currentUser?.let {
            db.collection("usernames").document(currentUser.uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userName = documentSnapshot.toObject(UserName::class.java)
                        val username = userName?.username

                        // Assuming textName is the TextView where you want to display the username
                        userText.text = "Welcome, $username!"
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("UserProfile", "Error getting data: $exception")
                }
        }

        homeViewModel.stepCount.observe(viewLifecycleOwner) { count ->
            // Update your TextView here
            stepCount.text = "Current daily step count: $count"
        }

        checkAndShowPopup()
        dateToWorkoutMap = mutableMapOf()

        loadWorkoutsFromFirestore { success, map ->
            if (success) {
                if (map.isNotEmpty()) {
                    dateToWorkoutMap.putAll(map)
                }
                else {
                    currentUser?.uid?.let { userId ->
                        initializeDateToWorkoutMapForNewUser(userId)
                    }
                }
                setUpCalendarListener(calendarView,selectedDateTextView)

            }
            else {
               // error case
            }
        }
        currentUser?.let {
            db.collection("user_profiles").document(currentUser.uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        userProfile = documentSnapshot.toObject(UserProfile::class.java)

                    } else {
                        userProfile = null
                    }
                }
                .addOnFailureListener { exception ->
                    userProfile = null
                }
        }

        return view
    }
    private fun checkAndShowPopup() {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser

        currentUser?.let { user ->
            // Get the document reference for the current user
            val userDocRef = db.collection("usernames").document(user.uid)

            // Get the document for the current user
            userDocRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        // Check if the popup has been shown before
                        val isPopupShown = document.getBoolean("popupShown") ?: false
                        if (!isPopupShown) {
                            // Show the popup if it hasn't been shown before
                            showPopupDialog()
                            Log.d("PopupCheck", "Popup is here.")

                        }
                        else {
                            Log.d("PopupCheck", "Popup has already been shown, no need to show it again.")
                        }
                    }
                    else {
                        // The document does not exist, handle the case where the user might be new
                        Log.d("PopupCheck", "Document does not exist, showing popup to new user.")
                        showPopupDialog()
                    }
                }
                else {
                    Log.e("UserProfile", "Error getting document: ", task.exception)
                }
            }
        } ?: Log.e("PopupCheck", "No current user found, cannot check popup status.")

    }

    private fun showPopupDialog() {
        val popupDialog = PopupDialog(requireContext(), this, homeViewModel)
        popupDialog.show()
    }
    private fun setUpCalendarListener(calendarView : CalendarView,selectedDateTextView: TextView){
        var workoutDayCounter = 0
        //val localDateToWorkoutMap = loadDateToWorkoutMap(requireContext()).toMutableMap()
        //Log.d("DateMap", "$dateToWorkoutMap")

        Log.d("UserProfile", "Data received successfully: $userProfile")

        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val today = Calendar.getInstance()
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            if (selectedDate.before(today)) {
                selectedDateTextView?.text = "Please select a future date"
            }
            else {
                if (userProfile != null) {
                    val formattedDate = "$year-${month + 1}-$dayOfMonth"
                    Log.d("today","$formattedDate")
                    var adjustedWorkoutIndex = 0
                    Log.d("SaveMap","Yes, it get's called.")

                    if (dateToWorkoutMap.isEmpty() || !dateToWorkoutMap.containsKey(formattedDate)) {

                        val selectedDay = getDaysDifference(today,selectedDate) % (7)

                        if (userProfile?.daysToTrain == 2 && (selectedDay == 0 || selectedDay == 1 || selectedDay == 3 || selectedDay==4 || selectedDay == 6)) {
                            dateToWorkoutMap[formattedDate] = Workout("Rest Day","","",0,"",0, emptyList(),0
                            )
                        }
                        if (userProfile?.daysToTrain == 3 && (selectedDay == 1 || selectedDay == 2 || selectedDay==4 || selectedDay == 6)) {
                            dateToWorkoutMap[formattedDate] = Workout("Rest Day","","",0,"",0, emptyList(),0
                            )
                        }
                        else if (userProfile?.daysToTrain == 4 && (selectedDay == 1 || selectedDay == 3 || selectedDay==5)){
                            dateToWorkoutMap[formattedDate] = Workout("Rest Day","","",0,"",0, emptyList(),0
                            )
                        }
                        else if (userProfile?.daysToTrain == 5 && (selectedDay == 1 || selectedDay == 4)){
                            dateToWorkoutMap[formattedDate] = Workout("Rest Day","","",0,"",0, emptyList(),0
                            )
                        }

                        else if (userProfile?.daysToTrain == 6 && selectedDay == 3){
                            dateToWorkoutMap[formattedDate] = Workout("Rest Day","","",0,"",0, emptyList(),0
                            )
                        }

                        else {
                            var specialDays = setOf<Int>()
                            if (userProfile?.daysToTrain == 2) {
                                specialDays = setOf(0, 1, 3, 4, 6)
                            }
                            if (userProfile?.daysToTrain == 3) {
                                specialDays = setOf(1, 2, 4, 6)
                            }
                            if (userProfile?.daysToTrain == 4) {
                                specialDays = setOf(1, 3, 5)
                            }
                            if (userProfile?.daysToTrain == 5) {
                                specialDays = setOf(1, 4)
                            }
                            if (userProfile?.daysToTrain == 6) {
                                specialDays = setOf(3)
                            }
                            if (userProfile?.daysToTrain == 7) {
                                specialDays = setOf()
                            }

                            Log.d("WorkoutIndex", "DaysToTrain: ${userProfile?.daysToTrain}")
                            val specialDaysBetween = countSpecialDaysBetween(today, selectedDate, specialDays)
                            adjustedWorkoutIndex = getDaysDifference(today,selectedDate) - specialDaysBetween

                            val workoutPlan = generateWorkoutPlan(userProfile!!)

                            val workoutIndex = adjustedWorkoutIndex
                            Log.d("SelectedDay", "${getDaysDifference(today,selectedDate)},${countSpecialDaysBetween(today,selectedDate,specialDays)}")
                            Log.d("SelectedDay", "$adjustedWorkoutIndex")
                            val recommendedWorkout1 = workoutPlan.getOrNull(workoutIndex)

                            if (recommendedWorkout1 != null) {
                                dateToWorkoutMap[formattedDate] = recommendedWorkout1
                            } else {
                                // Index is out of bounds, show "Adding soon" message
                                dateToWorkoutMap[formattedDate] = Workout("Adding soon", "", "", 0, "", 0, emptyList(),0)
                            }
                            workoutDayCounter++
                        }
                        Log.d("SaveMap", "Attempting to save dateToWorkoutMap for date: $formattedDate")
                        saveDateToWorkoutMap(requireContext(), dateToWorkoutMap)
                        currentUser?.uid?.let { userId ->
                            Log.d("FirestoreFetch", "Fetching document for user ID: $userId")
                            FirebaseFirestore.getInstance().collection("users").document(userId).get()
                                .addOnSuccessListener { documentSnapshot ->
                                    Log.d("FirestoreFetch", "Successfully fetched document for user ID: $userId")
                                    val existingMapData = documentSnapshot.data?.get("dateToWorkoutMap")
                                    if (existingMapData is Map<*, *>) {
                                        // Convert the raw map to a map with Workout values using Gson
                                        val gson = Gson()
                                        val type = object : TypeToken<Map<String, Workout>>() {}.type
                                        val existingMap: Map<String, Workout>?
                                        try {
                                            existingMap = gson.fromJson(gson.toJson(existingMapData), type)
                                        } catch (e: JsonSyntaxException) {
                                            Log.e("FirestoreConversion", "Error converting data to Map<String, Workout>", e)
                                            // Handle the case where existingMapData can't be converted to the expected type
                                            return@addOnSuccessListener
                                        }

                                        if (existingMap != null && !existingMap.containsKey(formattedDate)) {
                                            // If the date doesn't exist in Firestore, save the new workout map.
                                            Log.d("FirestoreWrite", "Attempting to write dateToWorkoutMap to Firestore for user ID: $userId")

                                            saveDateToWorkoutMapToFirestore(
                                                firestore = FirebaseFirestore.getInstance(),
                                                userId = userId,
                                                map = dateToWorkoutMap,
                                                callback = { success ->
                                                    if (success) {
                                                        Log.d("FirestoreWrite", "Workout map saved successfully.")
                                                    } else {
                                                        Log.e("FirestoreWrite", "Failed to save workout map.")
                                                    }
                                                }
                                            )
                                        } else {
                                            Log.d("FirestoreWrite", "Workout for $formattedDate already exists, skipping save.")
                                        }
                                    } else {
                                        //dateToWorkoutMap is not a map
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("FirestoreFetch", "Error fetching document for user ID: $userId", exception)
                                    //read error
                                }
                        }

                    }

                    selectedWorkout = dateToWorkoutMap[formattedDate]
                    if (selectedWorkout?.name == "Rest Day") {

                    }
                    if (completedWorkouts.contains(selectedWorkout)) {
                        dateToWorkoutMap[formattedDate] = Workout("Completed!","","",0,"",0, emptyList(),0)
                    }
                    selectedDateTextView?.text = selectedWorkout?.name ?: "No workout plan available"
                }

            }
            Log.d("DateMap", "$dateToWorkoutMap")

        }

    }
    fun initializeDateToWorkoutMapForNewUser(userId: String) {
        // Define a default structure for your dateToWorkoutMap
        val defaultMap = mutableMapOf<String, Workout>() // populate this as necessary
        val firestore = FirebaseFirestore.getInstance()

        // Save the default map to Firestore
        // Explicitly create the document with the default map
        firestore.collection("users").document(userId).set(mapOf("dateToWorkoutMap" to defaultMap))
            .addOnSuccessListener {
                Log.d("FirestoreInit", "New dateToWorkoutMap created successfully for user ID: $userId")
                // Continue with other operations, if necessary
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreInit", "Failed to create new dateToWorkoutMap for user ID: $userId", e)
            }
    }

    fun saveDateToWorkoutMapToFirestore(
        firestore: FirebaseFirestore,
        userId: String,
        map: Map<String, Workout>,
        callback: (Boolean) -> Unit
    ) {
        try {
            val docRef = firestore.collection("users").document(userId)
            Log.d("FirestoreSave", "Preparing to update document for user ID: $userId")
            docRef.update("dateToWorkoutMap", map)
                .addOnSuccessListener {
                    Log.d("FirestoreSave", "Document update successful for user ID: $userId")
                    callback(true)
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreSave", "Document update failed for user ID: $userId", e)
                    callback(false)
                }
        } catch (e: Exception) {
            Log.e("FirestoreSave", "Exception in saveDateToWorkoutMapToFirestore: ", e)
        }
    }

    private fun loadWorkoutsFromFirestore(callback: (Boolean, Map<String, Workout>) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            val docRef = FirebaseFirestore.getInstance().collection("users").document(userId)
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        val data = document.data as Map<String, Any>
                        val dateToWorkoutMap = data["dateToWorkoutMap"] as? Map<String, Map<String, Any>> ?: emptyMap()
                        val workouts = dateToWorkoutMap.mapValues { (_, workoutMap) ->
                            Workout(
                                name = workoutMap["name"] as? String ?: "",
                                difficulty = workoutMap["difficulty"] as? String ?: "",
                                type = workoutMap["type"] as? String ?: "",
                                rounds = (workoutMap["rounds"] as? Number)?.toInt() ?: 0,
                                rest = workoutMap["rest"] as? String ?: "",
                                position = (workoutMap["position"] as? Number)?.toInt() ?: 0,
                                exercises = (workoutMap["exercises"] as? List<Map<String, Any>>)?.map { exerciseMap ->
                                    Exercise(
                                        name = exerciseMap["name"] as? String ?: "",
                                        repetitionCount = exerciseMap["repetitionCount"] as? String ?: "",
                                        picturePath = exerciseMap["picturePath"] as? String ?: "",
                                        videoPath = exerciseMap["videoPath"] as? String ?: ""
                                    )
                                } ?: listOf(),
                                idetifier = (workoutMap["idetifier"] as? Number)?.toInt() ?: 0
                            )
                        }
                        callback(true, workouts)
                    } else {
                        // Document does not exist
                        callback(true, emptyMap())
                    }
                } else {
                    // The task failed, handle the error
                    callback(false, emptyMap())
                }
            }
        } ?: run {
            // Handle the case where there is no logged-in user
            callback(false, emptyMap())
        }
    }
    suspend fun loadDateToWorkoutMapFromFirestore(
            firestore: FirebaseFirestore,
            userId: String): Map<String, Workout> {
            return withContext(Dispatchers.IO) { // Switch to IO Dispatcher for network operation
                val task = firestore.collection("users").document(userId).get()
                try {
                    val snapshot = Tasks.await(task) //wait for the result
                    if (snapshot.exists()) {
                        val json = snapshot.get("dateToWorkoutMap")
                        val type = object : TypeToken<Map<String, Workout>>() {}.type
                        Gson().fromJson(json.toString(), type)
                    } else {
                        emptyMap()
                    }
                } catch (e: Exception) {
                    Log.e("LoadMap", "Error loading map", e)
                    emptyMap()
                }
            }
        }


    fun saveDateToWorkoutMap(context: Context, map: Map<String, Workout>) {
        val sharedPreferences = context.getSharedPreferences("map", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(map)
        editor.putString("dateToWorkoutMap", json)
        editor.apply()
    }
    fun loadDateToWorkoutMap(context: Context): Map<String, Workout> {
        val sharedPreferences = context.getSharedPreferences("map", Context.MODE_PRIVATE)
        val mapJson = sharedPreferences.getString("dateToWorkoutMap", null) ?: return emptyMap()
        val type = object : TypeToken<Map<String, Workout>>() {}.type
        return if (mapJson != null) {
            // If data exists, convert JSON to map
            val type = object : TypeToken<Map<String, Workout>>() {}.type
            Gson().fromJson(mapJson, type)
        } else {
            // Return an empty map if no data is found
            emptyMap()
        }
    }

    private fun getDaysDifference(start: Calendar, end: Calendar): Int {
        val startTime = start.timeInMillis
        val endTime = end.timeInMillis
        val diffTime = endTime - startTime
        return (diffTime / (1000 * 60 * 60 * 24)).toInt()
    }
    private fun countSpecialDaysBetween(start: Calendar, end: Calendar, specialDays: Set<Int>): Int {
        var count = 0
        val currentDate = start.clone() as Calendar
        while (currentDate <= end) {
            val daysDifference = getDaysDifference(start, currentDate)
            if (daysDifference % 7 in specialDays) {
                count++
            }
            currentDate.add(Calendar.DAY_OF_YEAR, 1)
        }
        return count
    }
    override fun onUsernameEntered(username: String) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            val userNameData = UserName(username, isPopupShown = true) // Set the flag to true
            db.collection("usernames")
                .document(currentUser.uid)
                .set(userNameData)
                .addOnSuccessListener {
                    Log.d("UserProfile", "Data saved successfully")
                }
                .addOnFailureListener { exception ->
                    Log.e("UserProfile", "Error saving data: $exception")
                }
        }
        Log.d("UsernameEntered", "Username entered: $username")

    }
    private fun fetchCompletedWorkouts(callback: (Boolean, List<Workout>) -> Unit) {
        Log.d("FetchWorkouts", "Starting Firestore query...")
        val workoutsCollectionRef = FirebaseFirestore.getInstance()
            .collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("completedWorkouts")

        workoutsCollectionRef.get()
            .addOnSuccessListener { documents ->
                Log.d("FetchWorkouts", "Query successful, processing documents...")
                val fetchedWorkouts = mutableListOf<Workout>()
                for (document in documents) {
                    Log.d("FetchWorkouts", "Document Data: ${document.data}")
                    val workoutMap = document.get("workout") as? Map<String, Any> ?: continue
                    val exercisesList = (workoutMap["exercises"] as? List<Map<String, Any>>)?.map { exerciseMap ->
                        Exercise(
                            name = exerciseMap["name"] as? String ?: "",
                            repetitionCount = exerciseMap["repetitionCount"] as? String ?: "",
                            picturePath = exerciseMap["picturePath"] as? String ?: "",
                            videoPath = exerciseMap["videoPath"] as? String ?: ""
                        )
                    } ?: listOf()

                    val workout = Workout(
                        name = workoutMap["name"] as? String ?: "",
                        difficulty = workoutMap["difficulty"] as? String ?: "",
                        type = workoutMap["type"] as? String ?: "",
                        rounds = (workoutMap["rounds"] as? Number)?.toInt() ?: 0,
                        rest = workoutMap["rest"] as? String ?: "",
                        position = (workoutMap["position"] as? Number)?.toInt() ?: 0,
                        exercises = exercisesList,
                        idetifier = (workoutMap["idetifier"] as? Number)?.toInt() ?: 0
                    )
                    Log.d("FetchWorkouts", "Created Workout: $workout")
                    fetchedWorkouts.add(workout)
                }
                Log.d("FetchWorkouts", "Processed ${fetchedWorkouts.size} workouts")
                callback(true,fetchedWorkouts)
            }
            .addOnFailureListener { exception ->
                Log.w("FetchWorkouts", "Error getting documents: ", exception)
                callback(false, mutableListOf())
            }
    }

    private fun updateUI(userProfile: UserProfile) {
        userProfile.let {
            val cardViewRecommended = view?.findViewById<ConstraintLayout>(R.id.RecommendedConstraintLayout)
            val selectedDateTextView = view?.findViewById<TextView>(R.id.selectedDateTextView)
            val recommendedWorkoutTextView = view?.findViewById<TextView>(R.id.tvREC)
            val recommendedWorkoutDiff = view?.findViewById<TextView>(R.id.tvRECDif)

            sharedPrefs = requireActivity().getSharedPreferences(sharedPrefsKey, Context.MODE_PRIVATE)
            //retrieveFromSharedPreferences()
            dateToWorkoutMap = mutableMapOf()
            loadWorkoutsFromFirestore { success, map ->
                if (success) {
                    if (map.isNotEmpty()) {
                        dateToWorkoutMap.putAll(map)
                        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-d"))
                        Log.d("today","$currentDate")
                        //val dateToWorkoutMap = loadDateToWorkoutMap(requireContext()).toMutableMap()
                        recommendedWorkout = dateToWorkoutMap[currentDate]
                        Log.d("RecWorkout", "$recommendedWorkout")

                    }
                }
                else {
                    // Handle the error case.
                }
            }
            Log.d("UserProfile", "$userProfile")

            if (userProfile != null) {
                val workoutPlan = generateWorkoutPlan(userProfile!!)

                selectedDateTextView?.setOnClickListener {
                    if (selectedDateTextView.text.isNotBlank()) {
                        if (selectedWorkout != null) {
                            if (selectedDateTextView.text == "Please select a future date"){

                            }
                            else {
                                val intent =
                                    Intent(requireContext(), WorkoutPreviewRecommended::class.java)
                                intent.putExtra("workout", selectedWorkout)
                                startActivity(intent)
                            }
                        }
                    }
                }



                cardViewRecommended?.setOnClickListener {
                    Log.d("CardViewClick", "CardView clicked.")

                    recommendedWorkout?.let { workout ->
                        Log.d("CardViewClick", "Workout is not null. Starting activity.")
                        val intent = Intent(requireContext(), WorkoutPreviewRecommended::class.java)
                        intent.putExtra("workout", workout)
                        startActivity(intent)
                    } ?: Log.d("CardViewClick", "Recommended workout is null.")
                }
                if (workoutPlan.isNotEmpty()) {
                    Log.d(
                        "CompletedWprkouts",
                        "Asd1 $completedWorkouts Size: ${completedWorkouts.size}"
                    )
                    if (completedWorkouts.contains(recommendedWorkout)) {
                        recommendedWorkoutTextView?.text = "Congratulations!"
                        val workoutImage = view?.findViewById<ImageView>(R.id.backgroundImageView)
                        workoutImage?.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        workoutImage?.setImageResource(R.drawable.completed)
                        recommendedWorkoutDiff?.text = ""
                        Log.d(
                            "CompletedWprkouts",
                            "Asd2 $completedWorkouts Size: ${completedWorkouts.size}"
                        )
                    }
                    else if (recommendedWorkoutTextView?.text == "Rest Day") {
                        recommendedWorkoutTextView?.text = recommendedWorkout?.name
                        val workoutImage = view?.findViewById<ImageView>(R.id.backgroundImageView)
                        workoutImage?.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        workoutImage?.setImageResource(R.drawable.rsz_clock)
                    }
                    else if (recommendedWorkoutTextView?.text == "Ab Workout") {
                        recommendedWorkoutTextView?.text = recommendedWorkout?.name
                        val workoutImage = view?.findViewById<ImageView>(R.id.backgroundImageView)
                        workoutImage?.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        workoutImage?.setImageResource(R.drawable.leg_raise)
                    }
                    else if (recommendedWorkoutTextView?.text == "Leg Workout") {
                        recommendedWorkoutTextView?.text = recommendedWorkout?.name
                        val workoutImage = view?.findViewById<ImageView>(R.id.backgroundImageView)
                        workoutImage?.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        workoutImage?.setImageResource(R.drawable.side_lunges)
                    }
                    else if (recommendedWorkoutTextView?.text == "Push Workout") {
                        recommendedWorkoutTextView?.text = recommendedWorkout?.name
                        val workoutImage = view?.findViewById<ImageView>(R.id.backgroundImageView)
                        workoutImage?.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        workoutImage?.setImageResource(R.drawable.clap_pushup)
                    }
                    else if (recommendedWorkoutTextView?.text == "Pull Workout") {
                        recommendedWorkoutTextView?.text = recommendedWorkout?.name
                        val workoutImage = view?.findViewById<ImageView>(R.id.backgroundImageView)
                        workoutImage?.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        workoutImage?.setImageResource(R.drawable.l_sit_pullup)
                    }
                    else {
                        Log.d(
                            "CompletedWprkouts",
                            "Asd3 $completedWorkouts Size: ${completedWorkouts.size}"
                        )
                        recommendedWorkoutTextView?.text = recommendedWorkout?.name
                        recommendedWorkoutDiff?.text = recommendedWorkout?.difficulty
                    }
                } else {
                    recommendedWorkoutTextView?.text = "No workout available"
                }
            }
        }
    }
    private fun fetchUserProfileAndWorkouts() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            FirebaseFirestore.getInstance().collection("user_profiles")
                .document(user.uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        userProfile = documentSnapshot.toObject(UserProfile::class.java)
                        userProfile?.let { profile ->
                            updateUI(profile)
                        }
                    }
                }
                .addOnFailureListener {
                    // Handle failure
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        homeViewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            userProfile?.let {
                updateUI(it)
            }
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            homeViewModel.fetchUserProfile(it.uid)
        }

        /* ez a legújabb komment, ha nem jó a mostani ez kell és removolni a felettieket
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            FirebaseFirestore.getInstance().collection("user_profiles")
                .document(user.uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        userProfile = documentSnapshot.toObject(UserProfile::class.java)
                        userProfile?.let { updateUI(it) }
                    }
                }
                .addOnFailureListener {
                    // Handle failure
                }
        }
         */

        Log.d("RecWorkout","$userProfile")


    }
    private fun fetchUserProfileData(onProfileFetched: (UserProfile?) -> Unit) {
        lifecycleScope.launch {
            val userProfile = fetchUserProfile()
            withContext(Dispatchers.Main) {
                onProfileFetched(userProfile)
            }
        }
    }



    suspend fun fetchUserProfile(): UserProfile? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return if (currentUser != null) {
            try {
                val documentSnapshot = FirebaseFirestore.getInstance()
                    .collection("user_profiles")
                    .document(currentUser.uid)
                    .get()
                    .await()

                if (documentSnapshot.exists()) {
                    documentSnapshot.toObject(UserProfile::class.java)
                } else {
                    null // User profile not found
                }
            } catch (e: Exception) {
                // Handle any exceptions (e.g., network errors)
                null
            }
        } else {
            null // Current user is null
        }
    }


    override fun onResume() {
        super.onResume()

        val userProfile = requireActivity().intent.getParcelableExtra<UserProfile>("userProfile")
        if (userProfile != null) {

            val workoutPlan = generateWorkoutPlan(userProfile)
            val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            //val dateToWorkoutMap = loadDateToWorkoutMap(requireContext()).toMutableMap()
            loadWorkoutsFromFirestore { success, map ->
                if (success) {
                    if (map.isNotEmpty()) {
                        dateToWorkoutMap.putAll(map)
                        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-d"))
                        Log.d("today","$currentDate")
                        recommendedWorkout = dateToWorkoutMap[currentDate]
                        Log.d("RecWorkout", "$recommendedWorkout")

                    }
                }
                else {
                    // Handle the error case.
                }
            }
            Log.d("RecWorkout", "$recommendedWorkout")

            if (workoutPlan.isNotEmpty()) {

                if (completedWorkouts.contains(recommendedWorkout)) {
                    recommendedWorkoutTextView.text = "Completed!"

                }
                else {

                    recommendedWorkoutTextView.text = recommendedWorkout?.name
                }
                Log.d("CompletedWprkouts", "Asd2 $completedWorkouts Size: ${completedWorkouts.size}")

            }
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("com.example.UPDATE_STEPS")
        stepCountReceiver = StepCountReceiver(homeViewModel)
        requireActivity().registerReceiver(stepCountReceiver, filter)    }

    override fun onStop() {
        super.onStop()
        if (this::stepCountReceiver.isInitialized) {
            requireActivity().unregisterReceiver(stepCountReceiver)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}