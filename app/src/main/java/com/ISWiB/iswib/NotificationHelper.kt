package com.ISWiB.iswib

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build

import androidx.core.app.NotificationCompat

/*
        This is Notification Helper class
        This class speeds up the process of creating notifications that sends user to map with given coordinates
        We can use it from any activity
        First we need to create channel
        We do that by using notification manager
 */

class NotificationHelper(base: Context) : ContextWrapper(base) {

    private var mManager: NotificationManager? = null

    //Here we create notification manager
    val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }

            return mManager
        }

    //Here we check if user has required api lvl
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    //Here we actually create channel
    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)

        //Enabling LED lights
        channel.enableLights(true)

        manager!!.createNotificationChannel(channel)
    }

    //Here we actually make notification that sends user to maps with given coordinates
    fun getChannelNotification(
        id: Int?, title: String, message: String, lat: Double?,
        lon: Double?
    ): NotificationCompat.Builder {
        //Creating uri to send it to map, and some required things
        val gmmIntentUri = Uri.parse("geo:$lat,$lon?q=$lat,$lon")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        val pendingIntent = PendingIntent.getActivity(this, id!!, mapIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        //Building notification
        return NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.icon_96)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }

    companion object {
        const val channelID = "channelID"
        const val channelName = "Schedule"
    }
}