package com.souravpalitrana.androiduicomponents
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class UniversityListActivity : AppCompatActivity() {
    private lateinit var adapter: UniversityAdapter
    private lateinit var searchEditText: TextInputEditText
    companion object {
        val universities = listOf(
            University(
                name = "University of Dhaka",
                location = "Dhaka",
                established = 1921,
                imageUrl = "https://example.com/dhaka-university.jpg",
                websiteUrl = "https://du.ac.bd",
                description = "Located in Dhaka, the capital of Bangladesh. It is the oldest university in the country, established in 1921.",
                departments = listOf("Mathematics", "Physics", "Chemistry", "Economics", "Law"),
                requiredSSCGpa = 4.5,
                requiredHSCGpa = 4.5,
                totalSeats = 5000,
                universityType = "Public",
                admissionTestRequired = true,
                admissionTestSubjects = listOf("English", "Mathematics", "General Knowledge"),
                admissionTestMarksDistribution = mapOf("English" to 40, "Mathematics" to 40, "General Knowledge" to 20),
                admissionTestSyllabus = "General subjects including English, Mathematics, and General Knowledge.",
                seatAvailability = mapOf("Science" to 2000, "Arts" to 1500, "Commerce" to 1500),
                admissionTestDate = "2024-01-15",
                admissionTestTime = "10:00 AM",
                admissionTestVenue = "Dhaka University Campus",
                admissionTestDuration = "2 hours",
                admissionTestTotalMarks = 100,
                admissionTestPassMarks = 40,
                admissionTestNegativeMarking = true,
                programSpecificGpaRequirements = mapOf(
                    "Mathematics" to mapOf("SSC" to 4.8, "HSC" to 4.8),
                    "Physics" to mapOf("SSC" to 4.6, "HSC" to 4.7),
                    "Economics" to mapOf("SSC" to 4.5, "HSC" to 4.5)
                ),
                quotaAdjustments = mapOf(
                    "Freedom Fighter Quota" to 10, // 10% of seats reserved
                    "Tribal Quota" to 5,          // 5% of seats reserved
                    "Physically Challenged Quota" to 3 // 3% of seats reserved
                ),
                additionalRequirements = mapOf(
                    "English" to "Minimum B grade in both SSC and HSC",
                    "Mathematics" to "Minimum A grade in HSC"
                )
            ),
            University(
                name = "Bangladesh University of Engineering and Technology",
                location = "Dhaka",
                established = 1962,
                imageUrl = "https://example.com/buet.jpg",
                websiteUrl = "https://buet.ac.bd",
                description = "One of the top engineering universities in Bangladesh.",
                departments = listOf("Civil Engineering", "Electrical Engineering", "Computer Science", "Architecture"),
                requiredSSCGpa = 4.75,
                requiredHSCGpa = 4.75,
                totalSeats = 1000,
                universityType = "Engineering",
                admissionTestRequired = true,
                admissionTestSubjects = listOf("Mathematics", "Physics", "Chemistry"),
                admissionTestMarksDistribution = mapOf("Mathematics" to 50, "Physics" to 30, "Chemistry" to 20),
                admissionTestSyllabus = "Engineering-specific syllabus focusing on Mathematics, Physics, and Chemistry.",
                seatAvailability = mapOf("Engineering" to 800, "Architecture" to 200),
                admissionTestDate = "2024-02-01",
                admissionTestTime = "9:00 AM",
                admissionTestVenue = "BUET Campus",
                admissionTestDuration = "3 hours",
                admissionTestTotalMarks = 100,
                admissionTestPassMarks = 50,
                admissionTestNegativeMarking = true,
                programSpecificGpaRequirements = mapOf(
                    "Civil Engineering" to mapOf("SSC" to 4.8, "HSC" to 4.8),
                    "Electrical Engineering" to mapOf("SSC" to 4.7, "HSC" to 4.8),
                    "Computer Science" to mapOf("SSC" to 4.7, "HSC" to 4.8)
                ),
                quotaAdjustments = mapOf(
                    "Freedom Fighter Quota" to 5, // 5% of seats reserved
                    "Tribal Quota" to 2,          // 2% of seats reserved
                    "Physically Challenged Quota" to 1 // 1% of seats reserved
                ),
                additionalRequirements = mapOf(
                    "Mathematics" to "Minimum A grade in both SSC and HSC",
                    "Physics" to "Minimum A grade in HSC"
                )
            ),
            University(
                name = "Chittagong University",
                location = "Chittagong",
                established = 1966,
                imageUrl = "https://example.com/chittagong-university.jpg",
                websiteUrl = "https://cu.ac.bd",
                description = "Located in Chittagong, known for its scenic campus surrounded by hills.",
                departments = listOf("Marine Science", "Economics", "Sociology", "Biology"),
                requiredSSCGpa = 4.0,
                requiredHSCGpa = 4.0,
                totalSeats = 4000,
                universityType = "Public",
                admissionTestRequired = true,
                admissionTestSubjects = listOf("Bangla", "English", "General Knowledge"),
                admissionTestMarksDistribution = mapOf("Bangla" to 30, "English" to 40, "General Knowledge" to 30),
                admissionTestSyllabus = "Comprehensive subjects focusing on Bangla, English, and General Knowledge.",
                seatAvailability = mapOf("Science" to 1500, "Arts" to 1500, "Commerce" to 1000),
                admissionTestDate = "2024-01-20",
                admissionTestTime = "11:00 AM",
                admissionTestVenue = "Chittagong University Campus",
                admissionTestDuration = "2 hours",
                admissionTestTotalMarks = 100,
                admissionTestPassMarks = 40,
                admissionTestNegativeMarking = false,
                programSpecificGpaRequirements = mapOf(
                    "Marine Science" to mapOf("SSC" to 4.2, "HSC" to 4.2),
                    "Economics" to mapOf("SSC" to 4.0, "HSC" to 4.0),
                    "Sociology" to mapOf("SSC" to 4.0, "HSC" to 4.0)
                ),
                quotaAdjustments = mapOf(
                    "Freedom Fighter Quota" to 10, // 10% of seats reserved
                    "Tribal Quota" to 5,          // 5% of seats reserved
                    "Physically Challenged Quota" to 3 // 3% of seats reserved
                ),
                additionalRequirements = mapOf(
                    "Bangla" to "Minimum B grade in both SSC and HSC",
                    "English" to "Minimum B grade in HSC"
                )
            ),
            // Add more universities as needed with similar structure
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_university_list)

        setupRecyclerView()
        setupSearch()
        setupClearButton()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.universitiesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UniversityAdapter(
            universities = universities,
            onItemClick = { university ->
                val intent = Intent(this, UniversityDetailsActivity::class.java)
                intent.putExtra("university", university)
                startActivity(intent)
            },
            onShortlistClick = { university, isShortlisted ->
                if (isShortlisted) {
                    ShortlistManager.addToShortlist(this, university.name)
                    Toast.makeText(this, "Added to shortlist", Toast.LENGTH_SHORT).show()
                } else {
                    ShortlistManager.removeFromShortlist(this, university.name)
                    Toast.makeText(this, "Removed from shortlist", Toast.LENGTH_SHORT).show()
                }
            }
        )
        recyclerView.adapter = adapter

        // Load initial shortlist state
        adapter.updateShortlistedUniversities(ShortlistManager.getShortlistedUniversities(this))
    }

    private fun setupSearch() {
        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterUniversities(s?.toString() ?: "")
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupClearButton() {
        findViewById<MaterialButton>(R.id.clearButton).setOnClickListener {
            searchEditText.text?.clear()
            adapter.updateList(universities)
        }
    }

    private fun filterUniversities(query: String) {
        val filteredList = if (query.isEmpty()) {
            universities
        } else {
            universities.filter { it.name.contains(query, ignoreCase = true) }
        }
        adapter.updateList(filteredList)
    }

    override fun onResume() {
        super.onResume()
        // Refresh shortlist state when returning to this screen
        adapter.updateShortlistedUniversities(ShortlistManager.getShortlistedUniversities(this))
    }
}