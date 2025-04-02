package com.example.foregroundservice



import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Find the Button view by its ID from the layout
        val btn = findViewById<Button>(R.id.startServiceButton)
        // Set up an OnClickListener on the button to start the service
        btn.setOnClickListener {
            val intent = Intent(this, MonitorService::class.java)
            //Check for Android version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // For Android Oreo or higher
                startForegroundService(intent)
            } else {
                // For older Android versions
                startService(intent)
            }
            finish()
        }
    }
}

