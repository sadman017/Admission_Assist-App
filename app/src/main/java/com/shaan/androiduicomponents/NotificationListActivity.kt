package com.shaan.androiduicomponents

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.shaan.androiduicomponents.adapters.NotificationAdapter
import com.shaan.androiduicomponents.managers.NotificationManager

class NotificationListActivity : AppCompatActivity() {
    private lateinit var adapter: NotificationAdapter
    private lateinit var emptyStateView: View
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)
        
        setupToolbar()
        setupRecyclerView()
        loadNotifications()
    }
    
    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Notifications"
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
    
    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.notificationsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = NotificationAdapter { notification ->
            notification.university?.let { university ->
                val intent = Intent(this, UniversityDetailsActivity::class.java)
                intent.putExtra("university", university)
                startActivity(intent)
            }
        }
        
        recyclerView.adapter = adapter
    }
    
    private fun loadNotifications() {
        val notifications = NotificationManager.getNotifications(this)
        adapter.updateNotifications(notifications)
        
        // Show empty state if no notifications
        emptyStateView = findViewById(R.id.emptyStateView)
        emptyStateView.visibility = if (notifications.isEmpty()) View.VISIBLE else View.GONE
    }
} 