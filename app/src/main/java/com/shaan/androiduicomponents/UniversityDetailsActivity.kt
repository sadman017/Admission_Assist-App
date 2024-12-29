package com.shaan.androiduicomponents

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shaan.androiduicomponents.models.University

class UniversityDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_university_details)

        val university: University? = intent.getSerializableExtra("university") as? University

        findViewById<TextView>(R.id.universityNameTextView).text = university?.generalInfo?.name
        findViewById<TextView>(R.id.universityLocationTextView).text = "Location: ${university?.generalInfo?.location}"
        findViewById<TextView>(R.id.universityEstablishedTextView).text = "Established: ${university?.generalInfo?.established}"
        findViewById<TextView>(R.id.universityDescriptionTextView).text = university?.generalInfo?.description
        findViewById<TextView>(R.id.universityTypeTextView).text = "Type: ${university?.generalInfo?.universityType}"
        findViewById<TextView>(R.id.universityAdmissionRequirementsTextView).text =  "Required SSC GPA: ${university?.admissionInfo?.requiredSSCGpa}, Required HSC GPA: ${university?.admissionInfo?.requiredHSCGpa}"
        findViewById<TextView>(R.id.universityTotalSeatsTextView).text = "Total Seats: ${university?.academicInfo?.totalSeats}"
        findViewById<TextView>(R.id.universityAdmissionTestInfoTextView).text = "Test Required: ${university?.admissionInfo?.admissionTestRequired}, Subjects: ${university?.admissionInfo?.admissionTestSubjects?.joinToString(", ")}"
        findViewById<TextView>(R.id.universityAdmissionTestSyllabusTextView).text = university?.admissionInfo?.admissionTestSyllabus
        findViewById<TextView>(R.id.universityAdmissionTestDateTextView).text = "Date: ${university?.admissionInfo?.admissionTestDetails?.date}"
        findViewById<TextView>(R.id.universityAdmissionTestTimeTextView).text = "Time: ${university?.admissionInfo?.admissionTestDetails?.time}"
        findViewById<TextView>(R.id.universityAdmissionTestVenueTextView).text = "Venue: ${university?.admissionInfo?.admissionTestDetails?.venue}"
        findViewById<TextView>(R.id.universityAdmissionTestDurationTextView).text = "Duration: ${university?.admissionInfo?.admissionTestDetails?.duration}"
        findViewById<TextView>(R.id.universityWebsiteTextView).text = university?.generalInfo?.websiteUrl
        findViewById<TextView>(R.id.programSpecificGpaTextView).text = university?.academicInfo?.programSpecificGpaRequirements?.map { (program, gpa) -> "$program: SSC ${gpa["SSC"]}, HSC ${gpa["HSC"]}" }?.joinToString("\n")
        findViewById<TextView>(R.id.quotaAdjustmentsTextView).text = university?.academicInfo?.programSpecificGpaRequirements
            ?.map { (program, gpa) -> "$program: SSC ${gpa["SSC"]}, HSC ${gpa["HSC"]}" }?.joinToString("\n")
        findViewById<TextView>(R.id.additionalRequirementsTextView).text = university?.additionalInfo?.additionalRequirements?.map { (subject, requirement) -> "$subject: $requirement" }?.joinToString("\n")
        findViewById<TextView>(R.id.applyButton).text = university?.additionalInfo?.applicationLink

        if (university == null) {
            Toast.makeText(this, "Error: University details not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        findViewById<TextView>(R.id.universityNameTextView).text = university.generalInfo.name
//        findViewById<TextView>(R.id.universityDescriptionTextView).text = university?.description
        findViewById<TextView>(R.id.universityLocationTextView).text = university.generalInfo.location
    }
}
