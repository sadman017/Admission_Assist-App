package com.shaan.androiduicomponents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.shaan.androiduicomponents.fragments.AdmissionGuideFragment
import com.shaan.androiduicomponents.fragments.HomeFragment
import com.shaan.androiduicomponents.helpers.NotificationHelper

class HomePage2 : AppCompatActivity() {
    companion object {
        private const val TAG = "HomePage2"
    }
    private lateinit var toolbar: MaterialToolbar
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home2)
        initializeViews()
        setupBottomNavigation()

        if (savedInstanceState == null) {
            loadFragment(HomeFragment.newInstance())
            toolbar.title = "Home"
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
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

    private fun initializeViews() {
        toolbar = "University Navigator".let { title ->
            findViewById<MaterialToolbar>(R.id.toolbar).apply {
                setTitle(title)
            }
        }
        bottomNavigation = findViewById(R.id.bottomNavigation)
        setSupportActionBar(toolbar)
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment.newInstance())
                    toolbar.title = "Home"
                    true
                }
                R.id.navigation_admission_guide -> {
                    loadFragment(AdmissionGuideFragment.newInstance())
                    toolbar.title = "Admission Guide"
                    true
                }
                R.id.navigation_profile -> {
                    showProfileDashboard()
                    false
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun showProfileDashboard() {
        val profileDashboardSheet = ProfileDashboardSheet()
        profileDashboardSheet.show(supportFragmentManager, "ProfileDashboard")
    }
}