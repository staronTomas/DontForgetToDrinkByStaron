package com.example.dont_forget_to_drink

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App : Application() {

    override fun onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel1 : NotificationChannel = NotificationChannel(
                Companion.CHANNEL_1_ID,
                "Water Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.enableVibration(true)
            channel1.enableLights(true)
            channel1.vibrationPattern
            channel1.description = "Please go drink some water"

            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(channel1)

        }
    }

    companion object {
        const val CHANNEL_1_ID = "channel1"
    }
}