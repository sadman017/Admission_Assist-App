package com.souravpalitrana.androiduicomponents.activityexample

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.view.WindowInsetsCompat
import com.souravpalitrana.androiduicomponents.R

class ExampleActivity : AppCompatActivity() {
    private var NOTIFICATION_PERMISSION_CODE = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_example)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        notify("onCreate")
        requestPermissions(this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            NOTIFICATION_PERMISSION_CODE
        )

        findViewById<Button>(R.id.button).setOnClickListener {
            val intent = Intent(this, SecondExampleActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        notify("onStart")
    }
    override fun onRestart() {
        super.onRestart()
        notify("onRestart")
    }

    override fun onStop() {
        super.onStop()
        notify("onStop")
    }
    override fun onDestroy() {
        super.onDestroy()
        notify("onDestroy")
    }

    override fun onResume() {
        super.onResume()
        notify("onResume")
    }

    override fun onPause() {
        super.onPause()
        notify("onPause")
    }

    fun notify(methodName: String) {
        val name = this.javaClass.name
        val strings = name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()

        val channelId = "default_channel_id"
        val channelName = "Default Channel"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // For devices running API 26+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Default channel for notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification builder
        val notificationBuilder =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Notification.Builder(this, channelId) // Use channelId for API 26+
            } else {
                Notification.Builder(this) // No channelId for API < 26
                    .setPriority(Notification.PRIORITY_DEFAULT) // Set priority for pre-Oreo
            }

        notificationBuilder
            .setContentTitle("$methodName  ${strings.last()}")
            .setContentText(name)
            .setSmallIcon(com.souravpalitrana.androiduicomponents.R.mipmap.ic_launcher)
            .setAutoCancel(true)

        // Display the notification
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

}