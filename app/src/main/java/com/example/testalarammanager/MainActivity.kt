package com.example.testalarammanager

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import android.widget.ToggleButton

class MainActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager
    private val NOTIFICATION_ID = 0
    private val NOTIFICATION_CHANNEL_ID = "com.example.testalarmmanager"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifyIntent = Intent(this, MyReceiver::class.java)
        val notifyPendingIntent = PendingIntent.getBroadcast(
                this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        createNotificationChannel()
        val alarmToggle = findViewById<ToggleButton>(R.id.alarmToggle)
        alarmToggle.setOnCheckedChangeListener { buttonView, isChecked ->
            val toastMessage: String
            if (isChecked) {
                val repeatInterval = 1000 * 180
                val triggerTime = SystemClock.elapsedRealtime() + repeatInterval
                toastMessage = "Stand Up Alarm on"
                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, repeatInterval.toLong(), notifyPendingIntent)
            } else {
                alarmManager.cancel(notifyPendingIntent)
                toastMessage = "Stand Up Alarm off"
            }
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }
    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Stand up notification", NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.apply {
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            description = "Notifies every 15 minutes to stand up and walk"
        }
        notificationManager.createNotificationChannel(notificationChannel)
    }

}