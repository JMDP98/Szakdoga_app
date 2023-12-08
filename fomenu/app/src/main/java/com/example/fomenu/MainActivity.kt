package com.example.fomenu

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.fomenu.databinding.ActivityMainBinding
import com.example.fomenu.question.UserProfile
import com.example.fomenu.question.question
import com.example.fomenu.ui.home.HomeFragment
import com.example.fomenu.ui.home.HomeViewModel
import com.example.fomenu.ui.my_progress.weight.WeightCheckReceiver
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val usernameTextView = headerView.findViewById<TextView>(R.id.usernameTextView)

       /* binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        val serviceIntent = Intent(this, StepResetService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
        startService(serviceIntent)
        scheduleDailyWeightCheck()
        createNotificationChannel()

        // Initialize ViewModel and observe username changes
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel?.username?.observe(this, Observer { username ->
            Log.d("MainActivityUser", "Username updated: $username")
            usernameTextView.text = username
        })

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_retake_quiz
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_retake_quiz -> {
                    showRetakeQuizConfirmation()
                    true
                }

                R.id.nav_logOut -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, login_page::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_gallery -> {
                    navController.navigate(R.id.nav_gallery)
                    true
                }

                R.id.nav_home -> {
                    navController.navigate(R.id.nav_home)
                    true
                }

                else -> {
                    false
                }
            }
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment

        // Initialize the BottomNavigationView
        val bottomNavView: BottomNavigationView = findViewById(R.id.bot_nav_view)

        // Set up the BottomNavigationView with the NavController

        bottomNavView.setupWithNavController(navController)

        homeViewModel?.username?.observe(this, Observer { username ->
            Log.d("MainActivityUser", "Username updated: $username")
            usernameTextView.text = username
        })
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            homeViewModel.fetchUserProfile(it.uid)
        }
    }

    fun setFirstRunComplete(context: Context) {
        val sharedPreferences = context.getSharedPreferences("IsFirstRun", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("firstRun", true).apply()
        Log.d("LoginActivity", "First run set to complete")
        val updatedFirstRun = sharedPreferences.getBoolean("firstRun", false)
        Log.d("LoginActivity", "Updated firstRun value: $updatedFirstRun")


    }
    private fun showRetakeQuizConfirmation() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Retake Quiz")
        builder.setMessage("Are you sure you want to retake the quiz? All your planned workouts will be gone!")

        builder.setPositiveButton("Yes") { dialog, which ->
            retakeQuiz()
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun retakeQuiz() {
        // Implement the logic to retake the quiz here
        // For example, clearing data and starting the quiz activity
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            clearDateToWorkoutMapInFirestore(
                firestore = FirebaseFirestore.getInstance(),
                userId = it,
                callback = { success ->
                    if (success) {
                        Log.d("ClearMap", "Map cleared successfully.")
                    } else {
                        Log.e("ClearMap", "Failed to clear map.")
                    }
                }
            )
        }
        val intent = Intent(this, question::class.java)
        startActivity(intent)
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "weights_channel_id",
                "Weights",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
    fun clearDateToWorkoutMapInFirestore(
        firestore: FirebaseFirestore,
        userId: String,
        callback: (Boolean) -> Unit
    ) {
        val docRef = firestore.collection("users").document(userId)
        // Set the dateToWorkoutMap to null to clear it, or use an emptyMap if it should be an empty map
        docRef.update("dateToWorkoutMap",emptyMap<String, Workout>())
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e("ClearMap", "Failed to clear map: ${e.message}")
                callback(false)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    private fun scheduleDailyWeightCheck() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 22)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(this, WeightCheckReceiver::class.java)

        // Correct handling of PendingIntent mutability flag for different Android versions
        val pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, pendingIntentFlag)

        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("MainActivity", "Menu item clicked: ${item.itemId}")
        when (item.itemId) {
            R.id.navigation_home -> {

                // Handle the click event for the drawer menu item
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START)
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}