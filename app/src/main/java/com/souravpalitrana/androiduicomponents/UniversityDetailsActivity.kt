package com.souravpalitrana.androiduicomponents

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UniversityDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_university_details)

        // Retrieve the University object from the intent
        val university: University? = intent.getSerializableExtra("university") as? University

        findViewById<TextView>(R.id.universityNameTextView).text = university?.name
        findViewById<TextView>(R.id.universityLocationTextView).text = "Location: ${university?.location}"
        findViewById<TextView>(R.id.universityEstablishedTextView).text = "Established: ${university?.established}"
        findViewById<TextView>(R.id.universityDescriptionTextView).text = university?.description
        findViewById<TextView>(R.id.universityDepartmentsTextView).text = "Departments: ${university?.departments?.joinToString(", ")}"
        findViewById<TextView>(R.id.universityAdmissionRequirementsTextView).text =  "Required SSC GPA: ${university?.requiredSSCGpa}, Required HSC GPA: ${university?.requiredHSCGpa}"
        findViewById<TextView>(R.id.universityTotalSeatsTextView).text = "Total Seats: ${university?.totalSeats}"
        findViewById<TextView>(R.id.universityAdmissionTestInfoTextView).text = "Test Required: ${university?.admissionTestRequired}, Subjects: ${university?.admissionTestSubjects?.joinToString(", ")}"
        findViewById<TextView>(R.id.universityAdmissionTestSyllabusTextView).text = university?.admissionTestSyllabus
        findViewById<TextView>(R.id.universityAdmissionTestDateTextView).text = "Date: ${university?.admissionTestDate}"
        findViewById<TextView>(R.id.universityAdmissionTestTimeTextView).text = "Time: ${university?.admissionTestTime}"
        findViewById<TextView>(R.id.universityAdmissionTestVenueTextView).text = "Venue: ${university?.admissionTestVenue}"
        findViewById<TextView>(R.id.universityAdmissionTestDurationTextView).text = "Duration: ${university?.admissionTestDuration}"
        findViewById<TextView>(R.id.universityWebsiteTextView).text = university?.websiteUrl

        if (university == null) {
            Toast.makeText(this, "Error: University details not found", Toast.LENGTH_SHORT).show()
            finish() // Exit the activity if no data is received
            return
        }
        // Display the university details
        findViewById<TextView>(R.id.universityNameTextView).text = university?.name
//        findViewById<TextView>(R.id.universityDescriptionTextView).text = university?.description
        findViewById<TextView>(R.id.universityLocationTextView).text = university?.location
    }
}
