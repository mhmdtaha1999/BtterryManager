package com.example.batterymanager.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.batterymanager.databinding.ActivityMainBinding
import com.example.batterymanager.databinding.ActivitySplashBinding
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        var helplist = arrayOf(
            "Make your Battery Powerful",
            "Make your Batter safe",
            "Make your Battery Faster",
            "Analuse your Battery",
            "Manage your phone battery",
            "Notify when your phone is full charge")
        var i = 1
        for(i in 1..6)
        {
            textgenerator((i*1000).toLong(),helplist[i-1])
        }

        Timer().schedule(timerTask {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        },7000)
    }

    private fun textgenerator(delayTime:Long,helptxt:String)
    {
        Timer().schedule(
            timerTask {
                runOnUiThread(timerTask {
                    binding.helpText.text = helptxt
                }) }, delayTime)
    }
}