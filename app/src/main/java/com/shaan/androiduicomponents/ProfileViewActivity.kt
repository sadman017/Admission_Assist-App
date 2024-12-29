package com.shaan.androiduicomponents

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.shaan.androiduicomponents.helpers.FirebaseHelper
import com.shaan.androiduicomponents.models.User
import kotlinx.coroutines.launch

class ProfileViewActivity : AppCompatActivity() {
    private lateinit var profileImage: ImageView
    private lateinit var nameText: TextView
    private lateinit var emailText: TextView
    private lateinit var mobileText: TextView
    private lateinit var dobText: TextView
    private lateinit var genderText: TextView
    private lateinit var nationalityText: TextView
    
    // Academic Info
    private lateinit var sscInstitutionText: TextView
    private lateinit var sscYearText: TextView
    private lateinit var sscBoardText: TextView
    private lateinit var sscGpaText: TextView
    
    private lateinit var hscInstitutionText: TextView
    private lateinit var hscYearText: TextView
    private lateinit var hscBoardText: TextView
    private lateinit var hscGpaText: TextView
    
    // Preferences
    private lateinit var universitiesText: TextView
    private lateinit var subjectsText: TextView
    private lateinit var examCategoriesText: TextView
    
    private lateinit var loadingView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)

        setupToolbar()
        initializeViews()
        loadUserData()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profile"
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initializeViews() {
        profileImage = findViewById(R.id.profileImage)
        nameText = findViewById(R.id.nameText)
        emailText = findViewById(R.id.emailText)
        mobileText = findViewById(R.id.mobileText)
        dobText = findViewById(R.id.dobText)
        genderText = findViewById(R.id.genderText)
        nationalityText = findViewById(R.id.nationalityText)
        
        // Academic Info
        sscInstitutionText = findViewById(R.id.sscInstitutionText)
        sscYearText = findViewById(R.id.sscYearText)
        sscBoardText = findViewById(R.id.sscBoardText)
        sscGpaText = findViewById(R.id.sscGpaText)
        
        hscInstitutionText = findViewById(R.id.hscInstitutionText)
        hscYearText = findViewById(R.id.hscYearText)
        hscBoardText = findViewById(R.id.hscBoardText)
        hscGpaText = findViewById(R.id.hscGpaText)
        
        // Preferences
        universitiesText = findViewById(R.id.universitiesText)
        subjectsText = findViewById(R.id.subjectsText)
        examCategoriesText = findViewById(R.id.examCategoriesText)
        
        loadingView = findViewById(R.id.loadingView)
    }

    private fun loadUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        lifecycleScope.launch {
            try {
                showLoading()
                val result = FirebaseHelper.getUserData(currentUser.uid)
                val userData = result.getOrThrow()
                displayUserData(userData)
                hideLoading()
            } catch (e: Exception) {
                hideLoading()
                Toast.makeText(this@ProfileViewActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun displayUserData(user: User) {
        // Load profile image
        if (user.profilePhotoUrl.isNotEmpty()) {
            Glide.with(this)
                .load(user.profilePhotoUrl)
                .circleCrop()
                .into(profileImage)
        }

        // Personal Info
        nameText.text = user.personalInfo.fullName
        emailText.text = user.email
        mobileText.text = user.contactInfo.mobileNumber
        dobText.text = user.personalInfo.dateOfBirth
        genderText.text = user.personalInfo.gender
        nationalityText.text = user.personalInfo.nationality

        // Academic Info - SSC
        sscInstitutionText.text = user.academicInfo.ssc.institutionName
        sscYearText.text = user.academicInfo.ssc.passingYear
        sscBoardText.text = user.academicInfo.ssc.board
        sscGpaText.text = user.academicInfo.ssc.gpa.toString()

        // Academic Info - HSC
        hscInstitutionText.text = user.academicInfo.hsc.institutionName
        hscYearText.text = user.academicInfo.hsc.passingYear
        hscBoardText.text = user.academicInfo.hsc.board
        hscGpaText.text = user.academicInfo.hsc.gpa.toString()

        // Preferences
        universitiesText.text = user.preferences.preferredUniversities.joinToString("\n")
        subjectsText.text = user.preferences.preferredSubjects.joinToString("\n")
        examCategoriesText.text = user.preferences.examCategories.joinToString("\n")
    }

    private fun showLoading() {
        loadingView.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingView.visibility = View.GONE
    }
} 