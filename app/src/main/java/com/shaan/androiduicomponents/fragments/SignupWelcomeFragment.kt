package com.shaan.androiduicomponents.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.shaan.androiduicomponents.Login
import com.shaan.androiduicomponents.R
import com.shaan.androiduicomponents.SignupActivity
import com.shaan.androiduicomponents.fragments.base.BaseSignupFragment

class SignupWelcomeFragment : BaseSignupFragment() {
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmPasswordInput: TextInputEditText
    private lateinit var loginPrompt: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        emailInput = view.findViewById(R.id.emailInput)
        passwordInput = view.findViewById(R.id.passwordInput)
        confirmPasswordInput = view.findViewById(R.id.confirmPasswordInput)
        loginPrompt = view.findViewById(R.id.loginPrompt)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        loginPrompt.setOnClickListener {
            try {
                val intent = Intent(requireContext(), Login::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                activity?.finish()
            } catch (e: Exception) {
                Log.e("SignupWelcomeFragment", "Error navigating to login: ${e.message}")
                Toast.makeText(context, "Unable to navigate to login page", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun isValid(): Boolean {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()
        val confirmPassword = confirmPasswordInput.text.toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = "Please enter a valid email address"
            return false
        }

        if (password.length < 6) {
            passwordInput.error = "Password must be at least 6 characters"
            return false
        }

        if (password != confirmPassword) {
            confirmPasswordInput.error = "Passwords do not match"
            return false
        }

        return true
    }

    override fun saveData() {
        (activity as? SignupActivity)?.apply {
            userEmail = emailInput.text.toString().trim()
            userPassword = passwordInput.text.toString()
        }
    }
} 