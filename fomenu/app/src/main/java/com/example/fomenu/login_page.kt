package com.example.fomenu

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AlertDialog
import com.example.fomenu.question.UserProfile
import com.example.fomenu.question.question
import com.example.fomenu.ui.home.HomeFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class login_page : AppCompatActivity() {

    override fun onBackPressed() {
        // Do nothing to block the back press
    }

    companion object {
        private const val additionalPadding = 20 // replace 20 with your actual padding value in pixels
    }

    private lateinit var client: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private var userProfile: UserProfile? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        if (!isNetworkAvailable(this)) {
            AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("An internet connection is required for this app. The app will close.")
                .setPositiveButton("OK") { _, _ -> finish() }
                .setOnDismissListener { finish() }
                .show()
        } else {

            val registerButton = findViewById<Button>(R.id.registerBtn)
            val email = findViewById<EditText>(R.id.etUsername)
            val password = findViewById<EditText>(R.id.etPassword)
            val loginbtn = findViewById<MaterialButton>(R.id.loginbtn)
            val googleBtn = findViewById<ImageView>(R.id.btnGoogle)
            val progressbar = findViewById<ProgressBar>(R.id.progressBar)

            password.setOnTouchListener(View.OnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    val drawableRight = password.compoundDrawables[2]
                    val drawableWidth = drawableRight.intrinsicWidth
                    val touchAreaStart = password.right - drawableWidth - additionalPadding
                    if (event.rawX >= touchAreaStart) {                        // Toggle password visibility
                        val selection = password.selectionEnd
                        if (password.transformationMethod == PasswordTransformationMethod.getInstance()) {
                            password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, R.drawable.show_password, 0)
                        } else {
                            password.transformationMethod = PasswordTransformationMethod.getInstance()
                            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, R.drawable.show_password, 0)
                        }
                        password.setSelection(selection)
                        return@OnTouchListener true
                    }
                }
                false
            })

            FirebaseApp.initializeApp(this)

            auth = Firebase.auth

            registerButton.setOnClickListener {
                val intent = Intent(this, Register::class.java)
                startActivity(intent)
                finish()
            }
            loginbtn.setOnClickListener {
                val emailText = email.text.toString()
                val passwordText = password.text.toString()

                if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                    signInWithEmail(emailText, passwordText)
                } else {
                    Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            client = GoogleSignIn.getClient(this, gso)

            googleBtn.setOnClickListener {
                GoogleSignIn.getClient(this, gso).signOut().addOnCompleteListener {
                    val intent = client.signInIntent
                    startActivityForResult(intent, 10001)
                }
            }
        }
    }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            //callbackManager.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 10001){
                Log.d("MYTAG", "Inside onActivityResult")
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null && idToken.isNotEmpty()) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            Log.d("MYTAG2", "Inside onActivityResult")
                            if (task.isSuccessful) {
                                isFirstRun(this) { isFirst ->
                                    if (isFirst) {
                                        val i = Intent(this, question::class.java)
                                        startActivity(i)
                                    } else {
                                        val mainIntent = Intent(this, MainActivity::class.java)
                                        startActivity(mainIntent)
                                    }
                                    finish()

                                }
                            }else {
                                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    private fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null && user.isEmailVerified) {
                        Log.d("Login", "signInWithEmail:success")
                        navigateToMain()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
    private fun navigateToMain() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }
    override fun onStart() {
        super.onStart()
        if (!isNetworkAvailable(this)) {
            AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("An internet connection is required for this app. The app will close.")
                .setPositiveButton("OK") { _, _ -> finish() }
                .setOnDismissListener { finish() }
                .show()
        } else {
            if (FirebaseAuth.getInstance().currentUser != null) {
                isFirstRun(this) { isFirst ->
                    if (isFirst) {
                        // First time running the app, navigate to questionnaire
                        val questionnaireIntent = Intent(this, question::class.java)
                        startActivity(questionnaireIntent)
                    } else {
                        // Not the first time, navigate to another activity
                        val mainIntent = Intent(this, MainActivity::class.java)
                        startActivity(mainIntent)
                    }
                }
                finish()
            }
        }
        /* val currentUser = auth.currentUser
         if (currentUser != null) {
             val i = Intent(this, Login::class.java)
             startActivity(i)
             finish()
         }*/
    }
    fun isFirstRun(context: Context, callback: (Boolean) -> Unit)  {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        currentUser?.let {
            db.collection("user_profiles").document(currentUser.uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        callback(false)
                    } else {
                        callback(true)
                    }
                }
                .addOnFailureListener { exception ->
                    callback(true)
                }
        } ?: run {
            // If currentUser is null, handle accordingly
            callback(true)
        }
    }

    }
