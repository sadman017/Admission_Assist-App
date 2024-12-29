package com.shaan.androiduicomponents.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shaan.androiduicomponents.R
import com.shaan.androiduicomponents.models.Notification
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationAdapter(
    private var notifications: List<Notification> = emptyList(),
    private val onNotificationClick: (Notification) -> Unit = {}
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.notificationTitle)
        val messageText: TextView = view.findViewById(R.id.notificationMessage)
        val timestampText: TextView = view.findViewById(R.id.notificationTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notifications[position]
        holder.titleText.text = notification.title
        holder.messageText.text = notification.message
        holder.timestampText.text = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
            .format(notification.timestamp)

        holder.itemView.setOnClickListener {
            onNotificationClick(notification)
        }
    }

    override fun getItemCount() = notifications.size

    fun updateNotifications(newNotifications: List<Notification>) {
        notifications = newNotifications
        notifyDataSetChanged()
    }
} 