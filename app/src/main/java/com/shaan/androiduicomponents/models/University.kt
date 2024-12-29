package com.shaan.androiduicomponents.models

import java.io.Serializable

data class University(
    val generalInfo: GeneralInfo,
    val academicInfo: UniAcademicInfo,
    val admissionInfo: AdmissionInfo,
    val additionalInfo: AdditionalInfo
) : Serializable

data class GeneralInfo(
    val name: String,
    val location: String,
    val established: Int,
    val imageUrl: String,
    val websiteUrl: String,
    val description: String,
    val universityType: String
) : Serializable

data class UniAcademicInfo(
    val departments: List<String>,
    val totalSeats: Int,
    val programSpecificGpaRequirements: Map<String, Map<String, Double>>,
    val quotaAdjustments: Map<String, Int>
) : Serializable

data class AdmissionInfo(
    val requiredSSCGpa: Double,
    val requiredHSCGpa: Double,
    val admissionTestRequired: Boolean,
    val admissionTestSubjects: List<String>,
    val admissionTestMarksDistribution: Map<String, Int>,
    val admissionTestSyllabus: String,
    val admissionTestDetails: AdmissionTestDetails
) : Serializable

data class AdmissionTestDetails(
    val date: String,
    val time: String,
    val venue: String,
    val duration: String,
    val totalMarks: Int,
    val passMarks: Int,
    val negativeMarking: Boolean
) : Serializable

data class AdditionalInfo(
    val seatAvailability: Map<String, Int>,
    val additionalRequirements: Map<String, String>,
    val applicationLink: String
) : Serializable
