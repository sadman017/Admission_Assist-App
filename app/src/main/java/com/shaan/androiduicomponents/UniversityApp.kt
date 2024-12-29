package com.shaan.androiduicomponents
import android.app.Application
import android.content.ComponentCallbacks2
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import android.util.Log
import com.google.firebase.firestore.BuildConfig
import com.google.firebase.firestore.FirebaseFirestore.setLoggingEnabled

class UniversityApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupFirebase()
    }

    private fun setupFirebase() {
        try {
            FirebaseApp.initializeApp(this)?.let { app ->
                val settings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(false)
                    .build()
                FirebaseFirestore.getInstance(app).apply {
                    firestoreSettings = settings
                    if (BuildConfig.DEBUG) {
                        setLoggingEnabled(true)
                    }
                }
                Log.d("UniversityApp", "Firebase initialized successfully")
            } ?: run {
                Log.e("UniversityApp", "Failed to initialize Firebase")
            }
        } catch (e: Exception) {
            Log.e("UniversityApp", "Error initializing Firebase", e)
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            System.gc()
        }
    }
} 