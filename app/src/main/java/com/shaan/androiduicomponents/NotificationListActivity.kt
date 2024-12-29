package com.shaan.androiduicomponents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.shaan.androiduicomponents.adapters.NotificationAdapter
import com.shaan.androiduicomponents.managers.NotificationManager

class NotificationListActivity : AppCompatActivity() {
    private lateinit var adapter: NotificationAdapter
    private lateinit var emptyStateView: View
    private lateinit var recyclerView: RecyclerView

    companion object {
        private const val TAG = "NotificationListActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)
        Log.d(TAG, "onCreate: Starting NotificationListActivity")
        
        try {
            initializeViews()
            setupToolbar()
            setupRecyclerView()
            loadNotifications()
        } catch (e: Exception) {
            Log.e(TAG, "onCreate: Error initializing activity", e)
            Toast.makeText(this, "Error loading notifications", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeViews() {
        Log.d(TAG, "initializeViews: Initializing views")
        try {
            recyclerView = findViewById(R.id.notificationsRecyclerView)
            emptyStateView = findViewById(R.id.emptyStateView)
        } catch (e: Exception) {
            Log.e(TAG, "initializeViews: Failed to initialize views", e)
            throw e
        }
    }
    
    private fun setupToolbar() {
        Log.d(TAG, "setupToolbar: Setting up toolbar")
        try {
            val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                title = "Notifications"
            }
            toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        } catch (e: Exception) {
            Log.e(TAG, "setupToolbar: Failed to setup toolbar", e)
            throw e
        }
    }
    
    private fun setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView: Setting up RecyclerView")
        try {
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = NotificationAdapter { notification ->
                notification.universityName?.let { university ->
                    val intent = Intent(this, UniversityDetailsActivity::class.java)
                    intent.putExtra("university", university)
                    startActivity(intent)
                }
            }
            recyclerView.adapter = adapter
        } catch (e: Exception) {
            Log.e(TAG, "setupRecyclerView: Failed to setup RecyclerView", e)
            throw e
        }
    }
    
    private fun loadNotifications() {
        Log.d(TAG, "loadNotifications: Loading notifications")
        try {
            val notifications = NotificationManager.getNotifications(this)
            Log.d(TAG, "loadNotifications: Found ${notifications.size} notifications")
            
            if (notifications.isEmpty()) {
                Log.d(TAG, "loadNotifications: No notifications found, showing empty state")
                showEmptyState()
            } else {
                Log.d(TAG, "loadNotifications: Displaying notifications")
                hideEmptyState()
                adapter.updateNotifications(notifications)
            }
        } catch (e: Exception) {
            Log.e(TAG, "loadNotifications: Failed to load notifications", e)
            showEmptyState()
            Toast.makeText(this, "Error loading notifications", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEmptyState() {
        Log.d(TAG, "showEmptyState: Showing empty state view")
        emptyStateView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideEmptyState() {
        Log.d(TAG, "hideEmptyState: Hiding empty state view")
        emptyStateView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Refreshing notifications")
        loadNotifications()
    }
} 