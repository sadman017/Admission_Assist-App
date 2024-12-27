package com.souravpalitrana.androiduicomponents
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupToolbar()
        setupGenderDropdown()
        setupDatePicker()
        setupSaveButton()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupGenderDropdown() {
        val genders = arrayOf("Male", "Female", "Other", "Prefer not to say")
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, genders)
        (findViewById<AutoCompleteTextView>(R.id.genderDropdown)).setAdapter(adapter)
    }

    private fun setupDatePicker() {
        val dobInput = findViewById<TextInputEditText>(R.id.dobInput)
        dobInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                dobInput.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
            }, year, month, day).show()
        }
    }

    private fun setupSaveButton() {
        findViewById<MaterialButton>(R.id.saveButton).setOnClickListener {
            // TODO: Implement save functionality
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // TODO: Implement settings
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
} 