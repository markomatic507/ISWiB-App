package com.ISWiB.iswib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/*
        AlertReceiver is used to set notifications
        It gets values from function start that is located in main activity
        Then it calls notification helper class to make notifications by sending values that were previously received
 */

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        //Getting values
        val title = intent.extras!!.getString("title")
        val message = intent.extras!!.getString("message")
        val lat = intent.extras!!.getDouble("lat")
        val lon = intent.extras!!.getDouble("lon")
        val id = intent.extras!!.getInt("id")

        /*
        Used for testing if alarm was set
        Log.e("IN START", "IN$title-$message")
        */

        // Calling notification helper
        val notificationHelper = NotificationHelper(context)
        val nb = notificationHelper.getChannelNotification(id, title, message, lat, lon)
        notificationHelper.manager?.notify(id, nb.build())
    }
}
