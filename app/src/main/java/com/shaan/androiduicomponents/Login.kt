package com.shaan.androiduicomponents

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class Login : AppCompatActivity() {
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var loadingView: View
    private lateinit var signupPrompt: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        initializeViews()
        setupListeners()
//        checkCurrentUser()
    }

    private fun initializeViews() {
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.btnLogin)
        loadingView = findViewById(R.id.loadingView)
        signupPrompt = findViewById(R.id.tvSignUp)
    }

    private fun setupListeners() {
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()

            if (validateInput(email, password)) {
                performLogin(email, password)
            }
        }
        signupPrompt.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = "Please enter a valid email address"
            return false
        }

        if (password.isEmpty()) {
            passwordInput.error = "Password cannot be empty"
            return false
        }

        return true
    }

    private fun performLogin(email: String, password: String) {
        showLoading()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                hideLoading()
                startActivity(Intent(this, HomePage2::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                hideLoading()
                Toast.makeText(this, "Login failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

//    private fun checkCurrentUser() {
//        if (FirebaseAuth.getInstance().currentUser != null) {
//            startActivity(Intent(this, HomePage::class.java))
//            finish()
//        }
//    }

    private fun showLoading() {
        loadingView.visibility = View.VISIBLE
        loginButton.isEnabled = false
    }

    private fun hideLoading() {
        loadingView.visibility = View.GONE
        loginButton.isEnabled = true
    }
}