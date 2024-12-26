package com.souravpalitrana.androiduicomponents
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EligibilityResultsActivity : AppCompatActivity() {
    private lateinit var adapter: UniversityAdapter

//    private val universities = listOf<University>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eligibility_results)

        val sscGpa = intent.getDoubleExtra("sscGpa", 0.0)
        val hscGpa = intent.getDoubleExtra("hscGpa", 0.0)
        val group = intent.getStringExtra("group") ?: ""
        val universityType = intent.getStringExtra("universityType")
        val preferredProgram = intent.getStringExtra("preferredProgram")
        val preferredLocation = intent.getStringExtra("preferredLocation")

        setupRecyclerView()
        findEligibleUniversities(sscGpa, hscGpa, group, universityType, preferredProgram, preferredLocation)
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.eligibleUniversitiesRecyclerView)
        adapter = UniversityAdapter(emptyList()) { university ->
            val intent = Intent(this, UniversityDetailsActivity::class.java)
            intent.putExtra("university", university)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun findEligibleUniversities(
        sscGpa: Double,
        hscGpa: Double,
        group: String,
        universityType: String?,
        preferredProgram: String?,
        preferredLocation: String?
    ) {
        // Filter universities based on eligibility criteria
        val eligibleUniversities = UniversityListActivity.universities.filter { university ->
            val meetsGpaCriteria = sscGpa>= university.requiredSSCGpa &&
                                 hscGpa >= university.requiredHSCGpa


            val meetsTypePreference = universityType == null ||
                                    universityType == "Both" ||
                                    university.universityType == universityType

            val meetsLocationPreference = preferredLocation == null ||
                                        university.location.contains(preferredLocation, true)

            // Add program-specific checks if needed
            val isEligible = meetsGpaCriteria && meetsTypePreference && meetsLocationPreference

            // Debug logs
            println("University: ${university.name}")
            println("meetsGpaCriteria: $meetsGpaCriteria")
            println("meetsTypePreference: $meetsTypePreference")
            println("meetsLocationPreference: $meetsLocationPreference")
            println("isEligible: $isEligible")

            isEligible
        }

        adapter.updateList(eligibleUniversities)

        if (eligibleUniversities.isEmpty()) {
            Toast.makeText(this, "No eligible universities found", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Eligible universities found", Toast.LENGTH_LONG).show()
            // Handle the list of eligible universities as needed
            // For example, you can display them in a RecyclerView
            // or perform further actions based on the list

        }
    }
} 