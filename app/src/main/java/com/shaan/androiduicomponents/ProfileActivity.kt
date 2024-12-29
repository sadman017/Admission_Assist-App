package com.shaan.androiduicomponents
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.shaan.androiduicomponents.helpers.FirebaseHelper
import com.shaan.androiduicomponents.models.PersonalInfo

import kotlinx.coroutines.launch
import java.util.Calendar

class ProfileActivity : AppCompatActivity() {
    private lateinit var profileImage: ShapeableImageView
    private lateinit var nameInput: TextInputEditText
    private lateinit var dobInput: TextInputEditText
    private lateinit var genderDropdown: AutoCompleteTextView
    private lateinit var mobileInput: TextInputEditText
    private lateinit var loadingView: View
    
    private var profilePhotoUri: Uri? = null
    private val PICK_IMAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupToolbar()
        initializeViews()
        setupGenderDropdown()
        setupDatePicker()
        setupClickListeners()
        loadUserData()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Profile"
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initializeViews() {
        profileImage = findViewById(R.id.profileImage)
        nameInput = findViewById(R.id.nameInput)
        dobInput = findViewById(R.id.dobInput)
        genderDropdown = findViewById(R.id.genderDropdown)
        mobileInput = findViewById(R.id.mobileInput)
        loadingView = findViewById(R.id.loadingView)

        findViewById<MaterialButton>(R.id.saveButton).setOnClickListener {
            if (validateInputs()) {
                saveUserData()
            }
        }
    }

    private fun setupGenderDropdown() {
        val genders = arrayOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, genders)
        genderDropdown.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        dobInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dobInput.setText(selectedDate)
            }, year, month, day).show()
        }
    }

    private fun setupClickListeners() {
        findViewById<MaterialButton>(R.id.changePhotoButton).setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
        }
    }

    private fun loadUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            showLoading()
            lifecycleScope.launch() {
                try {
                    val userData = FirebaseHelper.getUserData(currentUser.uid).getOrThrow()

                    nameInput.setText(userData.personalInfo.fullName)
                    dobInput.setText(userData.personalInfo.dateOfBirth)
                    genderDropdown.setText(userData.personalInfo.gender, false)

                    mobileInput.setText(userData.contactInfo.mobileNumber)

                    if (userData.profilePhotoUrl.isNotEmpty()) {
                        Glide.with(this@ProfileActivity)
                            .load(userData.profilePhotoUrl)
                            .placeholder(R.drawable.ic_profile_placeholder)
                            .error(R.drawable.ic_profile_placeholder)
                            .circleCrop()
                            .into(profileImage)
                    }

                    hideLoading()
                } catch (e: Exception) {
                    hideLoading()
                    Toast.makeText(this@ProfileActivity, 
                        "Error loading profile: ${e.message}", 
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        
        showLoading()
        lifecycleScope.launch {
            try {
                val profilePhotoUrl = profilePhotoUri?.let { uri ->
                    FirebaseHelper.uploadImage(currentUser.uid, uri, "profile").getOrThrow()
                }

                val existingData = FirebaseHelper.getUserData(currentUser.uid).getOrThrow()

                val updatedUser = existingData.copy(
                    personalInfo = PersonalInfo(
                        fullName = nameInput.text.toString().trim(),
                        dateOfBirth = dobInput.text.toString().trim(),
                        gender = genderDropdown.text.toString().trim(),
                        nationality = existingData.personalInfo.nationality,
                        nidNumber = existingData.personalInfo.nidNumber
                    ),
                    contactInfo = existingData.contactInfo.copy(
                        mobileNumber = mobileInput.text.toString().trim()
                    ),
                    profilePhotoUrl = profilePhotoUrl ?: existingData.profilePhotoUrl
                )

                FirebaseHelper.updateUserData(currentUser.uid, updatedUser).getOrThrow()

                hideLoading()
                Toast.makeText(this@ProfileActivity, 
                    "Profile updated successfully", 
                    Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                hideLoading()
                Toast.makeText(this@ProfileActivity, 
                    "Error updating profile: ${e.message}", 
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                profilePhotoUri = uri
                Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .into(profileImage)
            }
        }
    }

    private fun validateInputs(): Boolean {
        if (nameInput.text.isNullOrBlank()) {
            nameInput.error = "Name is required"
            return false
        }
        if (dobInput.text.isNullOrBlank()) {
            dobInput.error = "Date of Birth is required"
            return false
        }
        if (genderDropdown.text.isNullOrBlank()) {
            genderDropdown.error = "Gender is required"
            return false
        }
        if (mobileInput.text.isNullOrBlank()) {
            mobileInput.error = "Mobile number is required"
            return false
        }
        return true
    }

    private fun showLoading() {
        loadingView.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingView.visibility = View.GONE
    }
} 