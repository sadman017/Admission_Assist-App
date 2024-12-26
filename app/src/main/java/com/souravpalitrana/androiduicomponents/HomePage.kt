package com.souravpalitrana.androiduicomponents

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        findViewById<MaterialCardView>(R.id.universityFinderCard).setOnClickListener {
            startActivity(Intent(this, UniversityListActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.eligibilityCheckCard).setOnClickListener {
            // TODO: Navigate to EligibilityCheckActivity once created
            // startActivity(Intent(this, EligibilityCheckActivity::class.java))
        }
    }
}