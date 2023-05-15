package com.example.batterymanager.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.batterymanager.R
import com.example.batterymanager.adapter.BatteryUsageAdapter
import com.example.batterymanager.databinding.ActivityMainBinding
import com.example.batterymanager.databinding.ActivityUsageBatteryBinding
import com.example.batterymanager.model.BatteryModel
import com.example.batterymanager.utils.BatteryUsage
import kotlin.math.roundToInt

class UsageBatteryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsageBatteryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUsageBatteryBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)


        val batteryPercetArray: MutableList<BatteryModel> = ArrayList()
        val batteryUsage = BatteryUsage(this)

        for (item in batteryUsage.getUsageStateList())
        {
            Log.e("TAHA",item.packageName + " : " + item.totalTimeInForeground)

            if(item.totalTimeInForeground > 0){
                val bm = BatteryModel()
                bm.packageName=item.packageName
                bm.percentusage = (item.totalTimeInForeground.toFloat() / batteryUsage.gettotalTime().toFloat() * 100).toInt()
                batteryPercetArray += bm
            }
        }



        val adapter = BatteryUsageAdapter(this ,batteryPercetArray,batteryUsage.gettotalTime())
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager=LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
    }
}