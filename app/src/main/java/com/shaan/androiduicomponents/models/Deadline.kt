package com.shaan.androiduicomponents.models
import java.text.SimpleDateFormat
import java.util.*

data class Deadline(
    val universityName: String = "",
    val eventType: String = "",
    val date: String = "",
    val time: String = "",
    val status: String = determineStatus(date)
) {
    companion object {
        fun determineStatus(date: String): String {
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val deadlineDate = formatter.parse(date)
                val today = Date()
                
                return when {
                    deadlineDate == null -> "Upcoming"
                    deadlineDate.before(today) -> "Closed"
                    deadlineDate.time - today.time <= 7 * 24 * 60 * 60 * 1000 -> "Ongoing" // Within 7 days
                    else -> "Upcoming"
                }
            } catch (e: Exception) {
                return "Upcoming"
            }
        }
    }

    fun isUpcoming(): Boolean = status == "Upcoming"
    fun isOngoing(): Boolean = status == "Ongoing"
    fun isClosed(): Boolean = status == "Closed"
} 