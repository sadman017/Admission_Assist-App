package com.shaan.androiduicomponents.models

import java.util.Date

data class Notification(
    val title: String,
    val message: String,
    val timestamp: Date = Date(),
    val university: University? = null
) 