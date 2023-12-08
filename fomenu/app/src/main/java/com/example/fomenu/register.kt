package com.example.fomenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.fomenu.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = findViewById<EditText>(R.id.etPassword)
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPasswordAgain)
        val progressbar = findViewById<ProgressBar>(R.id.progressBar)

        auth = Firebase.auth

        binding.loginNow.setOnClickListener {
            val intent = Intent(this,login_page::class.java)
            startActivity(intent)
            finish()

        }
        binding.etPassword.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.etPassword.right - binding.etPassword.compoundDrawables[2].bounds.width())) {
                    // Toggle password visibility
                    val selection = binding.etPassword.selectionEnd
                    if (binding.etPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                        binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, R.drawable.show_password, 0)
                    } else {
                        binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, R.drawable.show_password, 0)
                    }
                    binding.etPassword.setSelection(selection)
                    return@OnTouchListener true
                }
            }
            false
        })
        binding.etPasswordAgain.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.etPasswordAgain.right - binding.etPasswordAgain.compoundDrawables[2].bounds.width())) {
                    // Toggle password visibility
                    val selection = binding.etPasswordAgain.selectionEnd
                    if (binding.etPasswordAgain.transformationMethod == PasswordTransformationMethod.getInstance()) {
                        binding.etPasswordAgain.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.etPasswordAgain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, R.drawable.show_password, 0)
                    } else {
                        binding.etPasswordAgain.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.etPasswordAgain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, R.drawable.show_password, 0)
                    }
                    binding.etPasswordAgain.setSelection(selection)
                    return@OnTouchListener true
                }
            }
            false
        })


        binding.buttonRegister.setOnClickListener {
            if(CheckFields()){


            progressbar.visibility = View.VISIBLE
            val emailRegis  = email.text.toString()
            //val userRegis  = user.text.toString()
            val pwRegis  = password.text.toString()
            if(TextUtils.isEmpty(emailRegis)){
                Toast.makeText(this,"Enter an email",Toast.LENGTH_LONG).show()
            }
            //if(TextUtils.isEmpty(userRegis)){
            //Toast.makeText(this,"Enter a username",Toast.LENGTH_LONG).show()
            //}
            if(TextUtils.isEmpty(pwRegis)){
                Toast.makeText(this,"Enter a password",Toast.LENGTH_LONG).show()
            }

            auth.createUserWithEmailAndPassword(emailRegis, pwRegis)
                .addOnCompleteListener(this) { task ->
                    progressbar.visibility = View.GONE
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Verification email sent.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        // Sign out the user until they verify their email
                        auth.signOut()

                    } else {
                        Toast.makeText(
                            this,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }

            }
        }

    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {
        TODO("Not yet implemented")
    }

    private fun CheckFields(): Boolean {
        val email = binding.etEmail.text.toString()
        if (binding.etEmail.text.toString() == ""){
            Toast.makeText(this,"Enter your email adress!",Toast.LENGTH_LONG).show()
            return false
        }
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Check your email again!",Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.etPassword.length() <= 8){
            Toast.makeText(this,"Password must be at least 8 characters!",Toast.LENGTH_LONG).show()
            return false
        }
        if(binding.etPassword.text.toString() == ""){
            Toast.makeText(this,"Enter a password!",Toast.LENGTH_LONG).show()
            return false
        }
        if(binding.etPasswordAgain.text.toString() == ""){
            Toast.makeText(this,"Confirm your password!",Toast.LENGTH_LONG).show()
            return false
        }
        if(binding.etPasswordAgain.text.toString() != binding.etPassword.text.toString()){
            Toast.makeText(this,"Password doesn't match!",Toast.LENGTH_LONG                                                                                                                                                                                                                                                               ).show()
            return false
        }
        return true

    }
}