package com.islam.otgtask.project_base.utils

import android.app.NotificationChannel
import android.content.Context
import android.os.Build

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.islam.otgtask.R

import java.util.ArrayList

object NotificationManager {

    fun initNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channels = ArrayList<NotificationChannel>()

            val channelNames = context.resources.getStringArray(R.array.notifications_channel_names)
            val channelDescription = context.resources.getStringArray(R.array.notifications_channel_description)
            val importance = android.app.NotificationManager.IMPORTANCE_DEFAULT

            for (i in channelNames.indices) {
                val channel = NotificationChannel(i.toString(), channelNames[i], importance)
                channel.description = channelDescription[i]
                channels.add(channel)
            }

            val notificationManager = context.getSystemService(android.app.NotificationManager::class.java)
            notificationManager.createNotificationChannels(channels)
        }
    }

    fun showNotification(context: Context, channelId: String, title: String, content: String) {
        var channelId = channelId
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            channelId = ""
        }

        // Create notification builder.
        val builder = NotificationCompat.Builder(context, channelId)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setWhen(System.currentTimeMillis())
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.priority = NotificationCompat.PRIORITY_HIGH

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())

    }

}
