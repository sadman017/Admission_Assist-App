package com.shaan.androiduicomponents.managers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shaan.androiduicomponents.models.Notification

object NotificationManager {
    private const val PREF_NAME = "notifications_preferences"
    private const val KEY_NOTIFICATIONS = "saved_notifications"
    private val gson = Gson()

    fun addNotification(context: Context, notification: Notification) {
        val notifications = getNotifications(context).toMutableList()
        notifications.add(0, notification) // Add to beginning of list
        saveNotifications(context, notifications)
    }

    fun getNotifications(context: Context): List<Notification> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_NOTIFICATIONS, "[]")
        val type = object : TypeToken<List<Notification>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveNotifications(context: Context, notifications: List<Notification>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = gson.toJson(notifications)
        prefs.edit().putString(KEY_NOTIFICATIONS, json).apply()
    }

    fun clearNotifications(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
} 