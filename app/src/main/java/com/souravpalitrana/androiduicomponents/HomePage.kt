package com.souravpalitrana.androiduicomponents

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.appbar.MaterialToolbar

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupToolbar()
        setupClickListeners()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_menu)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notifications -> {
                Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_profile -> {
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupClickListeners() {
        findViewById<MaterialCardView>(R.id.universityFinderCard).setOnClickListener {
            startActivity(Intent(this, UniversityListActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.eligibilityCheckCard).setOnClickListener {
            // TODO: Navigate to EligibilityCheckActivity once created
             startActivity(Intent(this, EligibilityCheckActivity::class.java))
        }
    }
}