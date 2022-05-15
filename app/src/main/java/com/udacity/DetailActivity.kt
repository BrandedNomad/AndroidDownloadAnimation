package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var confirmButton: Button
    lateinit var filenameContent: TextView
    lateinit var statusContent: TextView

    lateinit var navIntent: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val bundle = intent.extras

        var status = bundle?.getBoolean("status")
        var filename = bundle?.getString("filename")

        filenameContent = findViewById(R.id.fileNameContent)
        filenameContent.setText(when(filename){
            "Glide" -> getString(R.string.glideDownload)
            "Starter" -> getString(R.string.appDownload)
            "Retrofit" -> getString(R.string.retroDownload)
            else -> {"No File"}
        })

        statusContent = findViewById(R.id.statusContent)
        if(status == true){
            statusContent.text = "Success"
            statusContent.setTextColor(Color.GREEN)
        }else{
            statusContent.text = "Failed"
            statusContent.setTextColor(Color.RED)
        }

        val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
        notificationManager.cancelNotifications()







        confirmButton = findViewById(R.id.DetailsButton)

        confirmButton.setOnClickListener{
            navigateHome()
        }
    }

    fun navigateHome(){

        navIntent = Intent(applicationContext,MainActivity::class.java)
        startActivity(navIntent)
    }

}
