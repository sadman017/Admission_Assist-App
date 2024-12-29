package com.shaan.androiduicomponents
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.shaan.androiduicomponents.helpers.FirebaseHelper
import com.shaan.androiduicomponents.models.University
import kotlinx.coroutines.launch

class ShortlistActivity : AppCompatActivity() {
    private lateinit var adapter: UniversityAdapter
    private lateinit var emptyStateView: View
    private var shortlistedUniversities = listOf<University>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shortlist)

        setupToolbar()
        setupRecyclerView()
        loadShortlistedUniversities()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Shortlisted Universities"
        }
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.shortlistRecyclerView)
        emptyStateView = findViewById(R.id.emptyStateView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UniversityAdapter(
            universities = emptyList(),
            onItemClick = { university ->
                val intent = Intent(this, UniversityDetailsActivity::class.java)
                intent.putExtra("university", university)
                startActivity(intent)
            },
            onShortlistClick = { university, isShortlisted ->
                if (!isShortlisted) {
                    ShortlistManager.removeFromShortlist(this, university.generalInfo.name)
                    Toast.makeText(this,
                        "${university.generalInfo.name} removed from shortlist",
                        Toast.LENGTH_SHORT).show()
                    loadShortlistedUniversities()
                }
            }
        )
        recyclerView.adapter = adapter
    }

    private fun loadShortlistedUniversities() {
        lifecycleScope.launch {
            try {
                val allUniversities = FirebaseHelper.fetchUniversities().getOrThrow()
                shortlistedUniversities = ShortlistManager.getShortlistedUniversityList(this@ShortlistActivity, allUniversities)
                adapter.updateList(shortlistedUniversities)
                updateEmptyState()
            } catch (e: Exception) {
                Toast.makeText(this@ShortlistActivity,
                    "Error loading universities: ${e.message}",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateEmptyState() {
        emptyStateView.visibility = if (shortlistedUniversities.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        loadShortlistedUniversities()
    }
} 