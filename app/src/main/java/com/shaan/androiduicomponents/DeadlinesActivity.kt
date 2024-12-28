package com.shaan.androiduicomponents
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.shaan.androiduicomponents.models.Deadline
import java.time.LocalDate

class DeadlinesActivity : AppCompatActivity() {
    private lateinit var adapter: DeadlinesAdapter
    private lateinit var searchEditText: TextInputEditText
    private var deadlines = mutableListOf<Deadline>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deadlines)

        setupToolbar()
        setupRecyclerView()
        setupSearch()
        setupSortingOptions()
        loadDeadlines()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Deadlines"
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.deadlinesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DeadlinesAdapter(emptyList()) { deadline ->
            // Handle deadline click - maybe show details or edit
            showDeadlineDetails(deadline)
        }
        recyclerView.adapter = adapter
    }

    private fun setupSearch() {
        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterDeadlines(s?.toString() ?: "")
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupSortingOptions() {
        val sortButton = findViewById<MaterialButton>(R.id.sortButton)
        sortButton.setOnClickListener {
            showSortingDialog()
        }
    }

    private fun showSortingDialog() {
        val options = arrayOf("Date (Earliest)", "Date (Latest)", "University Name (A-Z)")
        MaterialAlertDialogBuilder(this)
            .setTitle("Sort By")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> sortByDate(ascending = true)
                    1 -> sortByDate(ascending = false)
                    2 -> sortByUniversityName()
                }
            }
            .show()
    }

    private fun sortByDate(ascending: Boolean) {
        deadlines.sortBy { LocalDate.parse(it.date) }
        if (!ascending) deadlines.reverse()
        adapter.updateList(deadlines)
    }

    private fun sortByUniversityName() {
        deadlines.sortBy { it.universityName }
        adapter.updateList(deadlines)
    }

    private fun filterDeadlines(query: String) {
        val filteredList = if (query.isEmpty()) {
            deadlines
        } else {
            deadlines.filter {
                it.universityName.contains(query, ignoreCase = true) ||
                it.type.contains(query, ignoreCase = true)
            }
        }
        adapter.updateList(filteredList)
    }

    private fun loadDeadlines() {
        // TODO: Load from database/storage
        deadlines = mutableListOf(
            Deadline("University of Dhaka", "Application Deadline", "2024-01-15"),
            Deadline("BUET", "Admission Test", "2024-02-01"),
            Deadline("Chittagong University", "Form Fill-up", "2024-01-20"),
            Deadline("Rajshahi University", "Application Deadline", "2024-01-25"),
            Deadline("Khulna University", "Admission Test", "2024-02-15")
        )
        adapter.updateList(deadlines)
    }

    private fun showDeadlineDetails(deadline: Deadline) {
        MaterialAlertDialogBuilder(this)
            .setTitle(deadline.universityName)
            .setMessage("""
                Type: ${deadline.type}
                Date: ${deadline.date}
            """.trimIndent())
            .setPositiveButton("Set Reminder") { _, _ ->
                // TODO: Implement reminder functionality
                Toast.makeText(this, "Reminder functionality coming soon", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Close", null)
            .show()
    }
}
