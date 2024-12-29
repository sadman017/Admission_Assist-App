package com.shaan.androiduicomponents.helpers

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.shaan.androiduicomponents.models.AcademicInfo
import com.shaan.androiduicomponents.models.AdditionalInfo
import com.shaan.androiduicomponents.models.AdmissionInfo
import com.shaan.androiduicomponents.models.AdmissionTestDetails
import com.shaan.androiduicomponents.models.GeneralInfo
import com.shaan.androiduicomponents.models.UniAcademicInfo
import com.shaan.androiduicomponents.models.User
import com.shaan.androiduicomponents.models.University
import kotlinx.coroutines.tasks.await

object FirebaseHelper {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private const val TAG = "FirebaseHelper"

    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: throw Exception("User ID not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveUserData(userId: String, user: User): Result<Unit> {
        return try {
            db.collection("users").document(userId)
                .set(user)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadImage(userId: String, imageUri: Uri, type: String): Result<String> {
        return try {
            val ref = storage.reference.child("users/$userId/$type.jpg")
            val uploadTask = ref.putFile(imageUri).await()
            val downloadUrl = ref.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserData(userId: String): Result<User> {
        return try {
            val document = db.collection("users").document(userId)
                .get()
                .await()
            val user = document.toObject(User::class.java)
                ?: throw Exception("User data not found")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserData(userId: String, user: User): Result<Unit> {
        return try {
            db.collection("users").document(userId)
                .set(user)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchUniversities(): Result<List<University>> {
        return try {
            Log.d(TAG, "Starting to fetch universities")
            val universities = mutableListOf<University>()
            val snapshot = db.collection("universitylist").get().await()
            
            Log.d(TAG, "Got ${snapshot.documents.size} universities from Firestore")
            
            for (document in snapshot.documents) {
                try {
                    Log.d(TAG, "Processing university document: ${document.id}")
                    
                    val data = document.data
                    if (data == null) {
                        Log.e(TAG, "Document ${document.id} has no data")
                        continue
                    }

                    val generalInfo = data["generalInfo"] as? Map<String, Any> 
                        ?: throw Exception("Missing generalInfo")
                    val academicInfo = data["academicInfo"] as? Map<String, Any> 
                        ?: throw Exception("Missing academicInfo")
                    val admissionInfo = data["admissionInfo"] as? Map<String, Any> 
                        ?: throw Exception("Missing admissionInfo")
                    val additionalInfo = data["additionalInfo"] as? Map<String, Any> 
                        ?: throw Exception("Missing additionalInfo")

                    val admissionTestDetails = (admissionInfo["admissionTestDetails"] as? Map<String, Any>)
                        ?: throw Exception("Missing admissionTestDetails")

                    val university = University(
                        generalInfo = GeneralInfo(
                            name = generalInfo["name"]?.toString() ?: "",
                            location = generalInfo["location"]?.toString() ?: "",
                            established = (generalInfo["established"] as? Long)?.toInt() ?: 0,
                            imageUrl = generalInfo["imageUrl"]?.toString() ?: "",
                            websiteUrl = generalInfo["websiteUrl"]?.toString() ?: "",
                            description = generalInfo["description"]?.toString() ?: "",
                            universityType = generalInfo["universityType"]?.toString() ?: ""
                        ),
                        academicInfo = UniAcademicInfo(
                            departments = (academicInfo["departments"] as? List<*>)?.mapNotNull { it?.toString() } 
                                ?: listOf(),
                            totalSeats = (academicInfo["totalSeats"] as? Long)?.toInt() ?: 0,
                            programSpecificGpaRequirements = (academicInfo["programSpecificGpaRequirements"] as? Map<String, Map<String, Any>>)?.mapValues { entry ->
                                entry.value.mapValues { (it.value as? Number)?.toDouble() ?: 0.0 }
                            } ?: mapOf(),
                            quotaAdjustments = (academicInfo["quotaAdjustments"] as? Map<String, Any>)?.mapValues { 
                                (it.value as? Long)?.toInt() ?: 0 
                            } ?: mapOf()
                        ),
                        admissionInfo = AdmissionInfo(
                            requiredSSCGpa = (admissionInfo["requiredSSCGpa"] as? Number)?.toDouble() ?: 0.0,
                            requiredHSCGpa = (admissionInfo["requiredHSCGpa"] as? Number)?.toDouble() ?: 0.0,
                            admissionTestRequired = admissionInfo["admissionTestRequired"] as? Boolean ?: false,
                            admissionTestSubjects = (admissionInfo["admissionTestSubjects"] as? List<*>)?.mapNotNull { 
                                it?.toString() 
                            } ?: listOf(),
                            admissionTestMarksDistribution = (admissionInfo["admissionTestMarksDistribution"] as? Map<String, Any>)?.mapValues { 
                                (it.value as? Long)?.toInt() ?: 0 
                            } ?: mapOf(),
                            admissionTestSyllabus = admissionInfo["admissionTestSyllabus"]?.toString() ?: "",
                            admissionTestDetails = AdmissionTestDetails(
                                date = admissionTestDetails["date"]?.toString() ?: "",
                                time = admissionTestDetails["time"]?.toString() ?: "",
                                venue = admissionTestDetails["venue"]?.toString() ?: "",
                                duration = admissionTestDetails["duration"]?.toString() ?: "",
                                totalMarks = (admissionTestDetails["totalMarks"] as? Long)?.toInt() ?: 0,
                                passMarks = (admissionTestDetails["passMarks"] as? Long)?.toInt() ?: 0,
                                negativeMarking = admissionTestDetails["negativeMarking"] as? Boolean ?: false
                            )
                        ),
                        additionalInfo = AdditionalInfo(
                            seatAvailability = (additionalInfo["seatAvailability"] as? Map<String, Any>)?.mapValues { 
                                (it.value as? Long)?.toInt() ?: 0 
                            } ?: mapOf(),
                            additionalRequirements = (additionalInfo["additionalRequirements"] as? Map<String, Any>)?.mapValues { 
                                it.value.toString() 
                            } ?: mapOf(),
                            applicationLink = additionalInfo["applicationLink"]?.toString() ?: ""
                        )
                    )
                    universities.add(university)
                    Log.d(TAG, "Successfully processed university: ${generalInfo["name"]}")
                } catch (e: Exception) {
                    Log.e(TAG, "Error processing university document ${document.id}: ${e.message}")
                    continue
                }
            }
            
            Log.d(TAG, "Successfully processed ${universities.size} universities")
            Result.success(universities)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching universities: ${e.message}", e)
            Result.failure(e)
        }
    }
} 