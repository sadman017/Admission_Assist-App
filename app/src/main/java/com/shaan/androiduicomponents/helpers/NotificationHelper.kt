package com.shaan.androiduicomponents.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.shaan.androiduicomponents.R
import com.shaan.androiduicomponents.UniversityDetailsActivity
import com.shaan.androiduicomponents.managers.NotificationManager as CustomNotificationManager
import com.shaan.androiduicomponents.models.University
import com.shaan.androiduicomponents.models.Notification

object NotificationHelper {
    private const val CHANNEL_ID = "university_notifications"
    private const val CHANNEL_NAME = "University Updates"
    private var notificationId = 0

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for university updates and shortlist changes"
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showShortlistNotification(context: Context, university: University, isAdded: Boolean) {
        val intent = Intent(context, UniversityDetailsActivity::class.java).apply {
            putExtra("university", university)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val title = if (isAdded) "University Shortlisted" else "University Removed"
        val message = if (isAdded) 
            "${university.name} has been added to your shortlist" 
        else 
            "${university.name} has been removed from your shortlist"

        val systemNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId++, systemNotification)

        // Save notification to storage
        val appNotification = Notification(
            title = title,
            message = message,
            university = university
        )
        CustomNotificationManager.addNotification(context, appNotification)
    }
} 