package com.shaan.androiduicomponents.models

data class User(
    val email: String = "",
    val personalInfo: PersonalInfo = PersonalInfo(),
    val contactInfo: ContactInfo = ContactInfo(),
    val academicInfo: AcademicInfo = AcademicInfo(),
    val preferences: AdmissionPreferences = AdmissionPreferences(),
    val profilePhotoUrl: String = "https://images.app.goo.gl/E4P3Pj5TRXmHZXp56",
    val signatureUrl: String = ""
)

data class PersonalInfo(
    val fullName: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val nationality: String = "",
    val nidNumber: String = ""
)

data class ContactInfo(
    val mobileNumber: String = "",
    val email: String = "",
    val emergencyContact: String = ""
)

data class AcademicInfo(
    val ssc: EducationDetails = EducationDetails(),
    val hsc: EducationDetails = EducationDetails()
)

data class EducationDetails(
    val institutionName: String = "",
    val passingYear: String = "",
    val board: String = "",
    val gpa: Float = 0f
)

data class AdmissionPreferences(
    val preferredUniversities: List<String> = emptyList(),
    val preferredSubjects: List<String> = emptyList(),
    val examCategories: List<String> = emptyList()
) 