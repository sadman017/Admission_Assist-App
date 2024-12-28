package com.shaan.androiduicomponents

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.shaan.androiduicomponents.helpers.NotificationHelper
import com.shaan.androiduicomponents.models.University

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
                ),
                applicationlink = "https://admission.du.ac.bd"
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
                ),
                applicationlink = "https://admission.buet.ac.bd"
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
                ),
                applicationlink = "https://admission.cu.ac.bd"
            ),
            University(
                name = "North South University",
                location = "Dhaka",
                established = 1992,
                imageUrl = "https://example.com/nsu.jpg",
                websiteUrl = "https://northsouth.edu",
                description = "A private university located in Dhaka, known for its business and technology programs.",
                departments = listOf("Business Administration", "Computer Science", "Economics", "Law"),
                requiredSSCGpa = 4.0,
                requiredHSCGpa = 4.0,
                totalSeats = 2000,
                universityType = "Private",
                admissionTestRequired = false,
                admissionTestSubjects = emptyList(),
                admissionTestMarksDistribution = emptyMap(),
                admissionTestSyllabus = "",
                seatAvailability = mapOf("Business Administration" to 800, "Computer Science" to 500, "Economics" to 400, "Law" to 300),
                admissionTestDate = "",
                admissionTestTime = "",
                admissionTestVenue = "",
                admissionTestDuration = "",
                admissionTestTotalMarks = 0,
                admissionTestPassMarks = 0,
                admissionTestNegativeMarking = false,
                programSpecificGpaRequirements = mapOf(
                    "Business Administration" to mapOf("SSC" to 4.2, "HSC" to 4.2),
                    "Computer Science" to mapOf("SSC" to 4.0, "HSC" to 4.0),
                    "Economics" to mapOf("SSC" to 4.0, "HSC" to 4.0)
                ),
                quotaAdjustments = emptyMap(),
                additionalRequirements = mapOf(
                    "Business Administration" to "Minimum B grade in both SSC and HSC",
                    "Computer Science" to "Minimum B grade in HSC"
                ),
                applicationlink = "https://admission.northsouth.edu"
            ),
            University(
                name = "American International University-Bangladesh",
                location = "Dhaka",
                established = 1994,
                imageUrl = "https://example.com/aiub.jpg",
                websiteUrl = "https://aiub.edu",
                description = "A private university located in Dhaka, known for its technology and business programs.",
                departments = listOf("Computer Science", "Business Administration", "Economics", "Law"),
                requiredSSCGpa = 4.0,
                requiredHSCGpa = 4.0,
                totalSeats = 1500,
                universityType = "Private",
                admissionTestRequired = false,
                admissionTestSubjects = emptyList(),
                admissionTestMarksDistribution = emptyMap(),
                admissionTestSyllabus = "",
                seatAvailability = mapOf("Computer Science" to 500, "Business Administration" to 400, "Economics" to 400, "Law" to 200),
                admissionTestDate = "",
                admissionTestTime = "",
                admissionTestVenue = "",
                admissionTestDuration = "",
                admissionTestTotalMarks = 0,
                admissionTestPassMarks = 0,
                admissionTestNegativeMarking = false,
                programSpecificGpaRequirements = mapOf(
                    "Computer Science" to mapOf("SSC" to 4.2, "HSC" to 4.2),
                    "Business Administration" to mapOf("SSC" to 4.0, "HSC" to 4.0),
                    "Economics" to mapOf("SSC" to 4.0, "HSC" to 4.0)
                ),
                quotaAdjustments = emptyMap(),
                additionalRequirements = mapOf(
                    "Computer Science" to "Minimum B grade in both SSC and HSC",
                    "Business Administration" to "Minimum B grade in HSC"
                ),
                applicationlink = "https://admission.aiub.edu"
            ),

            // Add more universities as needed with similar structure
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_university_list)

        checkNotificationPermission()

        setupRecyclerView()
        setupSearch()
        setupClearButton()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
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
                    NotificationHelper.showShortlistNotification(this, university, true)
                    val message = "${university.name} has been added to your shortlist"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                } else {
                    ShortlistManager.removeFromShortlist(this, university.name)
                    NotificationHelper.showShortlistNotification(this, university, false)
                    val message = "${university.name} has been removed from your shortlist"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
                adapter.updateShortlistedUniversities(ShortlistManager.getShortlistedUniversities(this))
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