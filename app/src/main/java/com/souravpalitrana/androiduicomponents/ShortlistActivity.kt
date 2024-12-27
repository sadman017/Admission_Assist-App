package com.souravpalitrana.androiduicomponents
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.souravpalitrana.androiduicomponents.UniversityAdapter

class ShortlistActivity : AppCompatActivity() {
    private lateinit var adapter: UniversityAdapter
    private lateinit var emptyStateView: View

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Shortlist"
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.shortlistRecyclerView)
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
                    ShortlistManager.removeFromShortlist(this, university.name)
                    loadShortlistedUniversities()
                    Toast.makeText(this, "Removed from shortlist", Toast.LENGTH_SHORT).show()
                }
            }
        )
        recyclerView.adapter = adapter

        emptyStateView = findViewById(R.id.emptyStateView)
    }

    private fun loadShortlistedUniversities() {
        val shortlistedUniversities = ShortlistManager.getShortlistedUniversityObjects(this)
        adapter.updateList(shortlistedUniversities)
        adapter.updateShortlistedUniversities(ShortlistManager.getShortlistedUniversities(this))
        
        // Show/hide empty state
        emptyStateView.visibility = if (shortlistedUniversities.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        loadShortlistedUniversities()
    }
} 