package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var URL: String
    private lateinit var downloadTitle: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            download()
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
                }
                R.id.app_download -> if(checked){
                    Log.e("MainActivity","load download selected")
                    URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
                    downloadTitle = "Project Starter Package"
                }
                R.id.retrofit_download -> if(checked){
                    Log.e("MainActivity","retrofit download selected")
                    URL = "https://github.com/square/retrofit.git"
                    downloadTitle = "Retrofit Package"
                }
            }
        }
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            Log.e("Reviever id",id.toString())
            if (downloadID == id) {
                //Toast.makeText(applicationContext, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private fun download() {
        val request = DownloadManager.Request(Uri.parse(URL))
                .setTitle(downloadTitle)
                .setDescription(getString(R.string.app_description))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)

        //Status Query
        var isComplete:Boolean = false
        var progress: Int = 0
        loop@ while(!isComplete){
            var cursor: Cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
            if(cursor.moveToFirst()){
                var status:Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                when(status){
                    DownloadManager.STATUS_FAILED -> {
                        isComplete = true
                        break@loop
                    }
                    DownloadManager.STATUS_PAUSED -> {

                    }
                    DownloadManager.STATUS_PENDING -> {

                    }
                    DownloadManager.STATUS_RUNNING -> {
                        var total:Long = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                        if(total >=0){
                            var downloaded:Long = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                            progress = ((downloaded*100L)/total).toInt()
                            Log.e("Progress",progress.toString())
                        }

                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        progress = 100
                        isComplete = true
                        Toast.makeText(applicationContext, "Download Completed", Toast.LENGTH_SHORT).show();
                        break@loop
                    }
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    companion object {
//        private const val URL =
//            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
