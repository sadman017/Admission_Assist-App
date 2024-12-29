import android.content.Context
import android.util.Log
import androidx.work.*
import com.google.firebase.firestore.FirebaseFirestore
import com.shaan.androiduicomponents.helpers.NotificationHelper
import com.shaan.androiduicomponents.models.*
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class DeadlineCheckWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val TAG = "DeadlineCheckWorker"
        private const val WORK_NAME = "deadline_check_worker"

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<DeadlineCheckWorker>(
                1, TimeUnit.DAYS,
                15, TimeUnit.MINUTES // Flex period
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
        }
    }

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "Starting deadline check")
            
            // Get shortlisted universities
            val shortlistedNames = applicationContext.getSharedPreferences(
                "university_prefs",
                Context.MODE_PRIVATE
            ).getStringSet("shortlisted_universities", emptySet()) ?: emptySet()

            if (shortlistedNames.isNotEmpty()) {
                // Fetch universities from Firebase
                val universities = FirebaseFirestore.getInstance()
                    .collection("universitylist")
                    .get()
                    .await()
                    .documents
                    .mapNotNull { doc ->
                        try {
                            // Explicitly convert document to University object
                            val generalInfo = doc.get("generalInfo") as? Map<String, Any>
                            val academicInfo = doc.get("academicInfo") as? Map<String, Any>
                            val admissionInfo = doc.get("admissionInfo") as? Map<String, Any>
                            val additionalInfo = doc.get("additionalInfo") as? Map<String, Any>

                            if (generalInfo != null && academicInfo != null && 
                                admissionInfo != null && additionalInfo != null) {
                                
                                val testDetails = (admissionInfo["admissionTestDetails"] as? Map<String, Any>)?.let {
                                    AdmissionTestDetails(
                                        date = it["date"]?.toString() ?: "",
                                        time = it["time"]?.toString() ?: "",
                                        venue = it["venue"]?.toString() ?: "",
                                        duration = it["duration"]?.toString() ?: "",
                                        totalMarks = (it["totalMarks"] as? Long)?.toInt() ?: 0,
                                        passMarks = (it["passMarks"] as? Long)?.toInt() ?: 0,
                                        negativeMarking = it["negativeMarking"] as? Boolean ?: false
                                    )
                                }

                                University(
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
                                        departments = (academicInfo["departments"] as? List<*>)?.mapNotNull { it?.toString() } ?: listOf(),
                                        totalSeats = (academicInfo["totalSeats"] as? Long)?.toInt() ?: 0,
                                        programSpecificGpaRequirements = (academicInfo["programSpecificGpaRequirements"] as? Map<String, Map<String, Double>>) ?: mapOf(),
                                        quotaAdjustments = (academicInfo["quotaAdjustments"] as? Map<String, Int>) ?: mapOf()
                                    ),
                                    admissionInfo = AdmissionInfo(
                                        requiredSSCGpa = (admissionInfo["requiredSSCGpa"] as? Double) ?: 0.0,
                                        requiredHSCGpa = (admissionInfo["requiredHSCGpa"] as? Double) ?: 0.0,
                                        admissionTestRequired = admissionInfo["admissionTestRequired"] as? Boolean ?: false,
                                        admissionTestSubjects = (admissionInfo["admissionTestSubjects"] as? List<*>)?.mapNotNull { it?.toString() } ?: listOf(),
                                        admissionTestMarksDistribution = (admissionInfo["admissionTestMarksDistribution"] as? Map<String, Int>) ?: mapOf(),
                                        admissionTestSyllabus = admissionInfo["admissionTestSyllabus"]?.toString() ?: "",
                                        admissionTestDetails = testDetails ?: AdmissionTestDetails(
                                            date = "", time = "", venue = "", duration = "",
                                            totalMarks = 0, passMarks = 0, negativeMarking = false
                                        )
                                    ),
                                    additionalInfo = AdditionalInfo(
                                        seatAvailability = (additionalInfo["seatAvailability"] as? Map<String, Int>) ?: mapOf(),
                                        additionalRequirements = (additionalInfo["additionalRequirements"] as? Map<String, String>) ?: mapOf(),
                                        applicationLink = additionalInfo["applicationLink"]?.toString() ?: ""
                                    )
                                )
                            } else null
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parsing university document", e)
                            null
                        }
                    }
                    .filter { it.generalInfo.name in shortlistedNames }

                // Check and notify deadlines
                NotificationHelper(applicationContext)
                    .checkAndNotifyDeadlines(universities)
            }

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error checking deadlines", e)
            Result.retry()
        }
    }
} 