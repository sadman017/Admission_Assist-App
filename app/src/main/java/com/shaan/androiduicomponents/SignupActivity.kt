package com.shaan.androiduicomponents

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shaan.androiduicomponents.adapters.SignupPagerAdapter
import com.shaan.androiduicomponents.fragments.SignupAcademicFragment
import com.shaan.androiduicomponents.fragments.SignupContactFragment
import com.shaan.androiduicomponents.fragments.SignupFinalFragment
import com.shaan.androiduicomponents.fragments.SignupPersonalFragment
import com.shaan.androiduicomponents.fragments.SignupPreferencesFragment
import com.shaan.androiduicomponents.fragments.base.BaseSignupFragment
import com.shaan.androiduicomponents.helpers.FirebaseHelper
import com.shaan.androiduicomponents.models.User
import com.shaan.androiduicomponents.models.PersonalInfo
import com.shaan.androiduicomponents.models.ContactInfo
import com.shaan.androiduicomponents.models.AcademicInfo
import com.shaan.androiduicomponents.models.AdmissionPreferences
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var previousButton: MaterialButton
    private lateinit var nextButton: MaterialButton
    private lateinit var pagerAdapter: SignupPagerAdapter
    var userEmail: String = ""
    var userPassword: String = ""
    val userData = mutableMapOf<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setupViews()
        setupViewPager()
        setupButtons()
    }

    private fun setupViews() {
        viewPager = findViewById(R.id.viewPager)
        previousButton = findViewById(R.id.previousButton)
        nextButton = findViewById(R.id.nextButton)
    }

    private fun setupViewPager() {
        pagerAdapter = SignupPagerAdapter(this)
        viewPager.adapter = pagerAdapter
        viewPager.isUserInputEnabled = false

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateNavigationButtons(position)
            }
        })
    }

    private fun setupButtons() {
        previousButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem > 0) {
                viewPager.currentItem = currentItem - 1
            }
        }

        nextButton.setOnClickListener {
            val currentFragment = pagerAdapter.getFragment(viewPager.currentItem)
            if (currentFragment.isValid()) {
                currentFragment.saveData()
                if (viewPager.currentItem == pagerAdapter.itemCount - 1) {
                    completeSignup()
                } else {
                    viewPager.currentItem = viewPager.currentItem + 1
                }
            }
        }

        updateNavigationButtons(0)
    }

    private fun updateNavigationButtons(position: Int) {
        previousButton.visibility = if (position == 0) View.GONE else View.VISIBLE
        
        nextButton.text = when (position) {
            pagerAdapter.itemCount - 1 -> getString(R.string.submit)
            else -> getString(R.string.next)
        }
    }

    private fun completeSignup() {
        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "Please complete all required fields", Toast.LENGTH_LONG).show()
            return
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Complete Registration")
            .setMessage("Are you sure you want to submit your registration?")
            .setPositiveButton("Submit") { _, _ ->
                lifecycleScope.launch {
                    try {
                        showLoading()

                        val createUserResult = FirebaseHelper.createUserWithEmailAndPassword(userEmail, userPassword)
                        val userId = createUserResult.getOrThrow()

                        val finalFragment = pagerAdapter.getFragment(5) as SignupFinalFragment

                        val profilePhotoUrl = finalFragment.profilePhotoUri?.let { uri ->
                            FirebaseHelper.uploadImage(userId, uri, "profile").getOrThrow()
                        } ?: ""

                        val signatureUrl = finalFragment.signatureUri?.let { uri ->
                            FirebaseHelper.uploadImage(userId, uri, "signature").getOrThrow()
                        } ?: ""

                        val personalFragment = pagerAdapter.getFragment(1) as SignupPersonalFragment
                        val contactFragment = pagerAdapter.getFragment(2) as SignupContactFragment
                        val academicFragment = pagerAdapter.getFragment(3) as SignupAcademicFragment
                        val preferencesFragment = pagerAdapter.getFragment(4) as SignupPreferencesFragment

                        val user = User(
                            email = userEmail,
                            personalInfo = personalFragment.getPersonalInfo(),
                            contactInfo = contactFragment.getContactInfo(),
                            academicInfo = academicFragment.getAcademicInfo(),
                            preferences = preferencesFragment.getPreferences(),
                            profilePhotoUrl = profilePhotoUrl,
                            signatureUrl = signatureUrl
                        )

                        FirebaseHelper.saveUserData(userId, user).getOrThrow()

                        hideLoading()
                        Toast.makeText(this@SignupActivity, "Registration successful!", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@SignupActivity, HomePage2::class.java))
                        finish()
                    } catch (e: Exception) {
                        hideLoading()
                        Toast.makeText(this@SignupActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton("Review") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showLoading() {
        findViewById<View>(R.id.loadingView)?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        findViewById<View>(R.id.loadingView)?.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Exit Registration")
                .setMessage("Are you sure you want to exit? All progress will be lost.")
                .setPositiveButton("Exit") { _, _ -> super.onBackPressed() }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .show()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
} 