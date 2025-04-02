package com.example.foregroundservice


import android.app.*
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

// Define the MonitorService class that extends the Service class
class MonitorService : Service() {

    private lateinit var receiver: InstallReceiver // The receiver to listen for package installations

    override fun onCreate() {
        super.onCreate()
        Log.d("ForegroundService", "Service started")
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()// Create a notification channel for devices
        startForeground(1, buildNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC) // Start the service as a foreground service with a notification
        receiver = InstallReceiver()
        // Set up an intent filter to listen for the ACTION_PACKAGE_ADDED broadcast
        val filter = IntentFilter(Intent.ACTION_PACKAGE_ADDED).apply {
            addDataScheme("package")
        }
        // Register the receiver to start listening for the intent
        registerReceiver(receiver, filter)
        // Return START_STICKY so that the service will restart if it's killed by the system
        return START_STICKY
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "monitor_channel",
                "App Monitor Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            // Get the system notification manager to create the channel
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
    // Function to build the notification to be shown in the foreground service
    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, "monitor_channel")
            .setContentTitle("Monitoring started")
            .setContentText("Listening for new installations...")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}