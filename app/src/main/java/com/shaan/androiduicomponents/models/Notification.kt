package com.shaan.androiduicomponents.models

import java.util.Date

data class Notification(
    val id: Long,
    val title: String,
    val message: String,
    val timestamp: Long,
    val type: String,
    val universityName: String? = null
) 