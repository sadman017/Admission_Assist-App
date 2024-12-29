package com.shaan.androiduicomponents

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.shaan.androiduicomponents.fragments.AdmissionGuideFragment
import com.shaan.androiduicomponents.fragments.HomeFragment
import com.shaan.androiduicomponents.helpers.NotificationHelper
import android.content.Context
import android.util.Log

class HomePage : AppCompatActivity() {
    companion object {
        private const val TAG = "HomePage"
    }

    private lateinit var toolbar: MaterialToolbar
    private lateinit var bottomNavigation: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Starting HomePage")
        try {
            setContentView(R.layout.activity_home)
            setupToolbar()
            setupClickListeners()
        } catch (e: Exception) {
            Log.e(TAG, "onCreate: Error initializing HomePage", e)
            Toast.makeText(this, "Error initializing app", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupToolbar() {
        Log.d(TAG, "setupToolbar: Setting up toolbar")
        try {
            val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar?.title = "Home"
        } catch (e: Exception) {
            Log.e(TAG, "setupToolbar: Failed to setup toolbar", e)
            throw e
        }
    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.toolbar)
//        bottomNavigation = findViewById(R.id.bottomNavigation)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "onCreateOptionsMenu: Creating options menu")
        try {
            menuInflater.inflate(R.menu.home_menu, menu)
            return true
        } catch (e: Exception) {
            Log.e(TAG, "onCreateOptionsMenu: Failed to create menu", e)
            return false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected: Menu item selected: ${item.title}")
        return try {
            when (item.itemId) {
                R.id.action_notifications -> {
                    Log.d(TAG, "onOptionsItemSelected: Starting NotificationListActivity")
                    startActivity(Intent(this, NotificationListActivity::class.java))
                    true
                }
//                R.id.action_profile -> {
//                    Log.d(TAG, "onOptionsItemSelected: Showing profile dashboard")
//                    showProfileDashboard()
//                    true
//                }
                else -> super.onOptionsItemSelected(item)
            }
        } catch (e: Exception) {
            Log.e(TAG, "onOptionsItemSelected: Error handling menu item selection", e)
            false
        }
    }

    private fun setupClickListeners() {
        Log.d(TAG, "setupClickListeners: Setting up card click listeners")
        try {
            findViewById<MaterialCardView>(R.id.universityFinderCard).setOnClickListener {
                Log.d(TAG, "Card clicked: University Finder")
                navigateToActivity(UniversityListActivity::class.java)
            }
            findViewById<MaterialCardView>(R.id.eligibilityCheckCard).setOnClickListener {
                Log.d(TAG, "Card clicked: Eligibility Check")
                navigateToActivity(EligibilityCheckActivity::class.java)
            }
            findViewById<MaterialCardView>(R.id.deadlinesCard).setOnClickListener {
                Log.d(TAG, "Card clicked: Deadlines")
                navigateToActivity(DeadlinesActivity::class.java)
            }
            findViewById<MaterialCardView>(R.id.shortlistCard).setOnClickListener {
                Log.d(TAG, "Card clicked: Shortlist")
                navigateToActivity(ShortlistActivity::class.java)
            }
        } catch (e: Exception) {
            Log.e(TAG, "setupClickListeners: Failed to setup click listeners", e)
            throw e
        }
    }

    private fun showProfileDashboard() {
        Log.d(TAG, "showProfileDashboard: Showing profile dashboard")
        try {
            val profileDashboard = ProfileDashboardSheet().apply {
                onLogoutClick = {
                    Log.d(TAG, "Logout clicked, handling logout process")
                    try {
                        dismiss()
                        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit().clear().apply()
                        Log.d(TAG, "User preferences cleared")

                        val intent = Intent(this@HomePage, Login::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                        startActivity(intent)
                        finish()
                        Log.d(TAG, "Successfully navigated to LoginActivity")
                    } catch (e: Exception) {
                        Log.e(TAG, "Error during logout process", e)
                        Toast.makeText(context, "Error logging out", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            profileDashboard.show(supportFragmentManager, ProfileDashboardSheet.TAG)
        } catch (e: Exception) {
            Log.e(TAG, "showProfileDashboard: Failed to show profile dashboard", e)
            Toast.makeText(this, "Error showing profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun <T : AppCompatActivity> navigateToActivity(activityClass: Class<T>) {
        Log.d(TAG, "navigateToActivity: Navigating to ${activityClass.simpleName}")
        try {
            startActivity(Intent(this, activityClass))
        } catch (e: Exception) {
            Log.e(TAG, "navigateToActivity: Failed to navigate to ${activityClass.simpleName}", e)
            Toast.makeText(this, "Error opening ${activityClass.simpleName}", Toast.LENGTH_SHORT).show()
        }
    }
}