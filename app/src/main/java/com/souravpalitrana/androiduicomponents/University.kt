package com.souravpalitrana.androiduicomponents

import java.io.Serializable

data class University(
    val name: String,
    val location: String,
    val established: Int,
    val imageUrl: String,
    val websiteUrl: String,
    val description: String,
    val departments: List<String>,
    val requiredSSCGpa: Double,
    val requiredHSCGpa: Double,
    val totalSeats: Int,
    val admissionTestRequired: Boolean,
    val admissionTestSubjects: List<String>,
    val admissionTestMarksDistribution: Map<String, Int>,
    val admissionTestSyllabus: String,
    val seatAvailability: Map<String, Int>,
    val admissionTestDate: String,
    val admissionTestTime: String,
    val admissionTestVenue: String,
    val admissionTestDuration: String,
    val admissionTestTotalMarks: Int,
    val admissionTestPassMarks: Int,
    val admissionTestNegativeMarking: Boolean,
    val programSpecificGpaRequirements: Map<String, Map<String, Double>>,
    val quotaAdjustments: Map<String, Int>,
    val additionalRequirements: Map<String, String>,
    val universityType: String,
): Serializable
