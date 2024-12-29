package com.shaan.androiduicomponents.managers

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shaan.androiduicomponents.models.Notification

object NotificationManager {
    private const val PREF_NAME = "notification_preferences"
    private const val KEY_NOTIFICATIONS = "notifications"
    private const val TAG = "NotificationManager"

    fun addNotification(context: Context, notification: Notification) {
        Log.d(TAG, "addNotification: Adding new notification: ${notification.title}")
        try {
            val notifications = getNotifications(context).toMutableList()
            notifications.add(0, notification)
            saveNotifications(context, notifications)
            Log.d(TAG, "addNotification: Successfully added notification")
        } catch (e: Exception) {
            Log.e(TAG, "addNotification: Failed to add notification", e)
            throw e
        }
    }

    fun getNotifications(context: Context): List<Notification> {
        Log.d(TAG, "getNotifications: Retrieving notifications")
        try {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val notificationsJson = sharedPreferences.getString(KEY_NOTIFICATIONS, "[]")
            Log.d(TAG, "getNotifications: Retrieved JSON: $notificationsJson")
            
            return try {
                val notifications = Gson().fromJson(notificationsJson, Array<Notification>::class.java).toList()
                Log.d(TAG, "getNotifications: Successfully parsed ${notifications.size} notifications")
                notifications
            } catch (e: Exception) {
                Log.e(TAG, "getNotifications: Failed to parse notifications JSON", e)
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getNotifications: Failed to retrieve notifications", e)
            return emptyList()
        }
    }

    private fun saveNotifications(context: Context, notifications: List<Notification>) {
        Log.d(TAG, "saveNotifications: Saving ${notifications.size} notifications")
        try {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val notificationsJson = Gson().toJson(notifications)
            Log.d(TAG, "saveNotifications: JSON to save: $notificationsJson")
            
            sharedPreferences.edit().putString(KEY_NOTIFICATIONS, notificationsJson).apply()
            Log.d(TAG, "saveNotifications: Successfully saved notifications")
        } catch (e: Exception) {
            Log.e(TAG, "saveNotifications: Failed to save notifications", e)
            throw e
        }
    }

    fun clearNotifications(context: Context) {
        Log.d(TAG, "clearNotifications: Clearing all notifications")
        try {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().remove(KEY_NOTIFICATIONS).apply()
            Log.d(TAG, "clearNotifications: Successfully cleared notifications")
        } catch (e: Exception) {
            Log.e(TAG, "clearNotifications: Failed to clear notifications", e)
            throw e
        }
    }
} 