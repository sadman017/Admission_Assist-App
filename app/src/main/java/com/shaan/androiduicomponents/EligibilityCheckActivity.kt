package com.shaan.androiduicomponents
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class EligibilityCheckActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eligibility_check)

        setupDropdowns()
        setupCheckButton()
    }

    private fun setupDropdowns() {
        // Setup Group Dropdown
        val groups = arrayOf("Science", "Humanities", "Business Studies")
        val groupAdapter = ArrayAdapter(this, R.layout.dropdown_item, groups)
        (findViewById<AutoCompleteTextView>(R.id.groupDropdown)).setAdapter(groupAdapter)

        // Setup University Type Dropdown
        val universityTypes = arrayOf("Public", "Private", "Both")
        val typeAdapter = ArrayAdapter(this, R.layout.dropdown_item, universityTypes)
        (findViewById<AutoCompleteTextView>(R.id.universityTypeDropdown)).setAdapter(typeAdapter)

        // Setup Program Dropdown
        val programs = arrayOf("Engineering", "Medicine", "Business", "Arts", "Science")
        val programAdapter = ArrayAdapter(this, R.layout.dropdown_item, programs)
        (findViewById<AutoCompleteTextView>(R.id.programDropdown)).setAdapter(programAdapter)

        // Setup Location Dropdown
        val locations = arrayOf("Dhaka", "Chittagong", "Rajshahi", "Khulna", "Sylhet", "Barisal")
        val locationAdapter = ArrayAdapter(this, R.layout.dropdown_item, locations)
        (findViewById<AutoCompleteTextView>(R.id.locationDropdown)).setAdapter(locationAdapter)
    }

    private fun setupCheckButton() {
        findViewById<MaterialButton>(R.id.checkEligibilityButton).setOnClickListener {
            if (validateInputs()) {
                checkEligibility()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val sscGpa = findViewById<TextInputEditText>(R.id.sscGpaInput).text.toString()
        val hscGpa = findViewById<TextInputEditText>(R.id.hscGpaInput).text.toString()
        
        if (sscGpa.isEmpty() || hscGpa.isEmpty()) {
            Toast.makeText(this, "Please enter both SSC and HSC GPA", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun checkEligibility() {
        val sscGpa = findViewById<TextInputEditText>(R.id.sscGpaInput).text.toString().toDoubleOrNull()
        val hscGpa = findViewById<TextInputEditText>(R.id.hscGpaInput).text.toString().toDoubleOrNull()
        val group = findViewById<AutoCompleteTextView>(R.id.groupDropdown).text.toString()
        val universityType = findViewById<AutoCompleteTextView>(R.id.universityTypeDropdown).text.toString()
        val preferredProgram = findViewById<AutoCompleteTextView>(R.id.programDropdown).text.toString()
        val preferredLocation = findViewById<AutoCompleteTextView>(R.id.locationDropdown).text.toString()

        if (sscGpa == null || hscGpa == null) {
            Toast.makeText(this, "Please enter valid GPA values", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, EligibilityResultsActivity::class.java).apply {
            putExtra("sscGpa", sscGpa)
            putExtra("hscGpa", hscGpa)
            putExtra("group", group)
            putExtra("universityType", universityType.takeIf { it.isNotEmpty() })
            putExtra("preferredProgram", preferredProgram.takeIf { it.isNotEmpty() })
            putExtra("preferredLocation", preferredLocation.takeIf { it.isNotEmpty() })
        }
        startActivity(intent)
    }
} 