package com.shaan.androiduicomponents.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.shaan.androiduicomponents.R
import com.shaan.androiduicomponents.UniversityDetailsActivity
import com.shaan.androiduicomponents.managers.NotificationManager as CustomNotificationManager
import com.shaan.androiduicomponents.models.Notification
import com.shaan.androiduicomponents.models.University
import java.text.SimpleDateFormat
import java.util.*

class NotificationHelper(private val context: Context) {
    companion object {
        private const val CHANNEL_ID = "university_notifications"
        private const val CHANNEL_NAME = "University Updates"
        private const val TAG = "NotificationHelper"

        const val NOTIFICATION_TYPE_SHORTLIST = "SHORTLIST"
        const val NOTIFICATION_TYPE_DEADLINE = "DEADLINE"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for university updates and deadlines"
            }
            
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showDeadlineNotification(university: University, daysRemaining: Int) {
        try {
            val testDetails = university.admissionInfo.admissionTestDetails
            val title = "Upcoming Deadline - ${university.generalInfo.name}"
            val message = when {
                daysRemaining > 1 -> "$daysRemaining days remaining for admission test on ${testDetails.date}"
                daysRemaining == 1 -> "Tomorrow is the admission test!"
                daysRemaining == 0 -> "Today is the admission test!"
                else -> return
            }

            val intent = Intent(context, UniversityDetailsActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("university", university)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                university.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(university.hashCode(), notification)

            val notificationItem = Notification(
                id = System.currentTimeMillis(),
                title = title,
                message = message,
                timestamp = System.currentTimeMillis(),
                type = NOTIFICATION_TYPE_DEADLINE,
                universityName = university.generalInfo.name
            )
            CustomNotificationManager.addNotification(context, notificationItem)

        } catch (e: Exception) {
            Log.e(TAG, "Error showing deadline notification", e)
        }
    }

    fun checkAndNotifyDeadlines(shortlistedUniversities: List<University>) {
        try {
            val today = Calendar.getInstance()
            
            shortlistedUniversities.forEach { university ->
                try {
                    val testDetails = university.admissionInfo.admissionTestDetails
                    val testDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .parse(testDetails.date)?.let { date ->
                            Calendar.getInstance().apply { time = date }
                        }

                    testDate?.let {
                        val daysRemaining = ((it.timeInMillis - today.timeInMillis) / 
                            (1000 * 60 * 60 * 24)).toInt()

                        when (daysRemaining) {
                            7, 3, 1, 0 -> showDeadlineNotification(university, daysRemaining)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error checking deadline for ${university.generalInfo.name}", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking deadlines", e)
        }
    }

    fun showShortlistNotification(university: University, isShortlisted: Boolean) {
        try {
            val title = if (isShortlisted) 
                "University Shortlisted" 
            else 
                "University Removed from Shortlist"
            
            val message = if (isShortlisted)
                "${university.generalInfo.name} has been added to your shortlist"
            else
                "${university.generalInfo.name} has been removed from your shortlist"

            val intent = Intent(context, UniversityDetailsActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("university", university)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                university.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(university.hashCode(), notification)

            val notificationItem = Notification(
                id = System.currentTimeMillis(),
                title = title,
                message = message,
                timestamp = System.currentTimeMillis(),
                type = NOTIFICATION_TYPE_SHORTLIST,
                universityName = university.generalInfo.name
            )
            CustomNotificationManager.addNotification(context, notificationItem)

        } catch (e: Exception) {
            Log.e(TAG, "Error showing shortlist notification", e)
        }
    }
} 