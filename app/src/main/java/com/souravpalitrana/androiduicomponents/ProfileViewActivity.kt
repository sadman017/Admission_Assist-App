package com.souravpalitrana.androiduicomponents
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.souravpalitrana.androiduicomponents.ProfileActivity

class ProfileViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)
        setContentView(R.layout.activity_profile_view)
        setupToolbar()
//        loadProfileData()
        findViewById<View>(R.id.editProfileButton).setOnClickListener {
            onEditClick()
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarpv)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
//        supportActionBar?.title = "Profile"
    }

    private fun loadProfileData() {
        // TODO: Load profile data from storage/database

    }

    fun onEditClick() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }
} 