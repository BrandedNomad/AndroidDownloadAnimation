package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var URL: String
    private lateinit var downloadTitle: String
    private lateinit var fileName:String
    private var complete = false
    private var isSelected:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(CHANNEL_ID,"Download")

        custom_button.setOnClickListener {
            if(isSelected){
                custom_button.buttonState = ButtonState.Completed
                download()
                complete = true
            }else{
                custom_button.buttonState = ButtonState.Clicked
                Toast.makeText(applicationContext,"Please select option",Toast.LENGTH_SHORT).show()
            }


        }

    }


    fun onRadioButtonSelected(view: View){
        if(view is RadioButton){
            val checked = view.isChecked
            when (view.getId()){
                R.id.glide_download -> if(checked){
                    Log.e("MainActivity","glide download selected")
                    URL = "https://github.com/bumptech/glide.git"
                    downloadTitle = "Glide Package"
                    fileName = "Glide"
                    isSelected = true
                }
                R.id.app_download -> if(checked){
                    Log.e("MainActivity","load download selected")
                    URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
                    downloadTitle = "Project Starter Package"
                    fileName = "Starter"
                    isSelected = true
                }
                R.id.retrofit_download -> if(checked){
                    Log.e("MainActivity","retrofit download selected")
                    URL = "https://github.com/square/retrofit.git"
                    downloadTitle = "Retrofit Package"
                    fileName = "Retrofit"
                    isSelected = true
                }
                else{
                    isSelected = false
                }
            }
        }
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (downloadID == id) {
                Toast.makeText(applicationContext, "Download Completed", Toast.LENGTH_SHORT).show();
                if(complete){

                    custom_button.hasCompletedDownload()
                    Log.e("firstFileName",fileName.toString())
                    Log.e("firstStatus",complete.toString())
                    notificationManager.sendNotification(downloadTitle,"Download completed",context!!,complete,fileName)
                    complete = false
                }
            }
        }
    }


    private fun download() {
        val request = DownloadManager.Request(Uri.parse(URL))
                .setTitle(downloadTitle)
                .setDescription(getString(R.string.app_description))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)

    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun createChannel(channelId:String,channelName:String){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW).apply{
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor= Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Your download is ready"


            notificationManager = getSystemService(
                NotificationManager::class.java
            ) as NotificationManager

            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

    companion object {
        private const val CHANNEL_ID = "package_download_channel_ID"
    }

}
