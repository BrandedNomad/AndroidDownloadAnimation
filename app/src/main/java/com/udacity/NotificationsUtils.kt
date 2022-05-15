package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat

private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(messageTitle:String, messageBody:String, applicationContext: Context,status:Boolean,fileName:String) {

    //bundle
    val bundle: Bundle = Bundle()
    bundle.putBoolean("status",status)
    bundle.putString("filename",fileName)

    //intent
    val detailsIntent = Intent(applicationContext,DetailActivity::class.java)
    detailsIntent.putExtras(bundle)

    //PendingIntent
    val detailsPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        detailsIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    //build notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        "package_download_channel_ID"
    )
        .setContentTitle(messageTitle)
        .setSmallIcon(R.drawable.cloud_download_48)
        .setContentText(messageBody)
        .setAutoCancel(true)
        .addAction(
            R.drawable.cloud_download_48,
            "Check Status",
            detailsPendingIntent
        )

    notify(NOTIFICATION_ID,builder.build())

}

fun NotificationManager.cancelNotifications(){
    cancelAll()
}