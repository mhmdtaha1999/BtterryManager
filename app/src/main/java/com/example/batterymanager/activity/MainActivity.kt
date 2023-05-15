package com.example.batterymanager.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.example.batterymanager.R
import com.example.batterymanager.utils.BatteryUsage
import com.example.batterymanager.databinding.ActivityMainBinding
import com.example.batterymanager.model.BatteryModel
import com.example.batterymanager.service.BatteryAlarmService
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        startservice()


        binding.imgMenu.setOnClickListener {
            binding.drawer.openDrawer(Gravity.RIGHT)
        }
        binding.incDrawer.txtInfo.setOnClickListener {
            startActivity(Intent(this@MainActivity, UsageBatteryActivity::class.java))
            binding.drawer.closeDrawer(Gravity.RIGHT)
        }

        registerReceiver(batteryInforeceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }


    private fun startservice(){
        val serviceIntent = Intent(this,BatteryAlarmService::class.java)
        ContextCompat.startForegroundService(this,serviceIntent)
    }

    private var batteryInforeceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            var batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            val batteryHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
            var batteryPlugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
            var batteryTemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10


            binding.circularProgressBar.progressMax = 100f
            binding.circularProgressBar.setProgressWithAnimation(batteryLevel.toFloat())

            binding.txtCharge.text = batteryLevel.toString() + " %"

            binding.txtVoltage.text =
                (intent.getDoubleExtra(BatteryManager.EXTRA_VOLTAGE, 0.0) / 1000).toString() + " V"

            binding.txtTechnology.text = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)

            //show Plug status
            if (batteryPlugged == 0) {
                binding.txtPlug.text = "Plug-out"
            } else {
                binding.txtPlug.text = "Plug-in"

            }
            //Show Temperature
            binding.txtTemperature.text = batteryTemp.toString() + " °C"

            when (batteryHealth) {
                BatteryManager.BATTERY_HEALTH_DEAD -> {
                    binding.txtHealth.text = "your battery is fully dead, please change it"
                    binding.txtHealth.setTextColor(Color.parseColor("#000000"))
                    binding.imgHealth.setImageResource(R.drawable.dead)
                }
                BatteryManager.BATTERY_HEALTH_GOOD -> {
                    binding.txtHealth.text = "your battery is Good, please take care of that"
                    binding.txtHealth.setTextColor(Color.GREEN)
                    binding.imgHealth.setImageResource(R.drawable.good)
                }
                BatteryManager.BATTERY_HEALTH_COLD -> {
                    binding.txtHealth.text = "your battery is cold, it's ok"
                    binding.txtHealth.setTextColor(Color.BLUE)
                    binding.imgHealth.setImageResource(R.drawable.cold)
                }
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> {
                    binding.txtHealth.text = "your battery is overheat, please don't use your phone"
                    binding.txtHealth.setTextColor(Color.RED)
                    binding.imgHealth.setImageResource(R.drawable.overheat)
                }
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> {
                    binding.txtHealth.text = "your battery is over voltage, please don't use your phone"
                    binding.txtHealth.setTextColor(Color.YELLOW)
                    binding.imgHealth.setImageResource(R.drawable.over_voltage)
                }
            }

        }
    }
}