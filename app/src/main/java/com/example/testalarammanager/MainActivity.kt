package com.example.testalarammanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        createNotificationChannel()
        val alarmToggle = findViewById<ToggleButton>(R.id.alarmToggle)
        alarmToggle.setOnCheckedChangeListener { buttonView, isChecked ->
            val toastMessage: String
            if (isChecked) {
                toastMessage = "Stand Up Alarm on"
                deliverNotification(this)
            } else {
                notificationManager.cancelAll()
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
    private fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
                context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Stand Up Alert")
                .setContentText("You should stand up and walk around now")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

        notificationManager.notify(NOTIFICATION_ID, notification)

    }
}