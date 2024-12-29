package com.shaan.androiduicomponents
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.shaan.androiduicomponents.models.University

class EligibilityResultsActivity : AppCompatActivity() {
    private lateinit var adapter: UniversityAdapter
//    private lateinit var emptyStateView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eligibility_results)

//        setupToolbar()
        setupRecyclerView()
        processEligibility()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Eligible Universities"
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun processEligibility() {
        val sscGpa = intent.getDoubleExtra("sscGpa", 0.0)
        val hscGpa = intent.getDoubleExtra("hscGpa", 0.0)
        val group = intent.getStringExtra("group") ?: ""
        val universityType = intent.getStringExtra("universityType")
        val preferredProgram = intent.getStringExtra("preferredProgram")
        val preferredLocation = intent.getStringExtra("preferredLocation")
        val universities = intent.getSerializableExtra("universities") as? ArrayList<University>

        if (universities == null) {
            Toast.makeText(this, "Error: No university data available", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        var meetsGpaCriteria: Boolean = false
        val eligibleUniversities = universities.filter { university ->
            meetsGpaCriteria = sscGpa >= university.admissionInfo.requiredSSCGpa &&
                                 hscGpa >= university.admissionInfo.requiredHSCGpa

            val meetsTypePreference = universityType == null || 
                                    universityType == "Both" || 
                                    university.generalInfo.universityType == universityType

            val meetsLocationPreference = preferredLocation == null || 
                                        preferredLocation.isEmpty() || 
                                        university.generalInfo.location.contains(preferredLocation, true)

            val meetsProgramRequirements = if (!preferredProgram.isNullOrEmpty()) {
                university.academicInfo.programSpecificGpaRequirements[preferredProgram]?.let { requirements ->
                    sscGpa >= (requirements["SSC"] ?: university.admissionInfo.requiredSSCGpa) &&
                    hscGpa >= (requirements["HSC"] ?: university.admissionInfo.requiredHSCGpa)
                } ?: meetsGpaCriteria

            } else {
                meetsGpaCriteria
            }
            meetsGpaCriteria
        }
        if(meetsGpaCriteria) {
            Log.d("Result", "SSC GPA: ${sscGpa}, HSC GPA: ${hscGpa}")
        }
        adapter.updateList(eligibleUniversities)
        updateEmptyState(eligibleUniversities.isEmpty())
    }

    private fun updateEmptyState(isEmpty: Boolean) {
//        emptyStateView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        if (isEmpty) {
            Toast.makeText(this, "No eligible universities found", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.eligibleUniversitiesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UniversityAdapter(
            universities = emptyList(),
            onItemClick = { university ->
                val intent = Intent(this, UniversityDetailsActivity::class.java)
                intent.putExtra("university", university)
                startActivity(intent)
            },
            onShortlistClick = { university, isShortlisted ->
                if (isShortlisted) {
                    ShortlistManager.addToShortlist(this, university.generalInfo.name)
                    Toast.makeText(this, "Added to shortlist", Toast.LENGTH_SHORT).show()
                } else {
                    ShortlistManager.removeFromShortlist(this, university.generalInfo.name)
                    Toast.makeText(this, "Removed from shortlist", Toast.LENGTH_SHORT).show()
                }
                // Update adapter immediately after shortlist change
                adapter.updateShortlistedUniversities(
                    ShortlistManager.getShortlistedUniversities(this)
                )
            }
        )
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.updateShortlistedUniversities(
            ShortlistManager.getShortlistedUniversities(this)
        )
    }
} 