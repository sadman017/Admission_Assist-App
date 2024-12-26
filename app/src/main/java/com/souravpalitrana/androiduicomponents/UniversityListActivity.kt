package com.souravpalitrana.androiduicomponents
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class UniversityListActivity : AppCompatActivity() {
    private lateinit var adapter: UniversityAdapter
    private lateinit var searchEditText: TextInputEditText
    private val universities = listOf(
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
            admissionTestNegativeMarking = true
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
            admissionTestNegativeMarking = true
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
            admissionTestNegativeMarking = false
        ),
        // Add more universities as needed with similar structure
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_university_list)

        setupRecyclerView()
        setupSearch()
        setupClearButton()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.universitiesRecyclerView)
        adapter = UniversityAdapter(universities) { university ->
            val intent = Intent(this, UniversityDetailsActivity::class.java)
            intent.putExtra("university", university)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
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
}