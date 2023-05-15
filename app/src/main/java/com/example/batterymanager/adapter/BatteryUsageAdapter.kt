package com.example.batterymanager.adapter

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.batterymanager.R
import com.example.batterymanager.model.BatteryModel
import kotlinx.coroutines.NonDisposableHandle.parent
import java.text.FieldPosition
import kotlin.math.roundToInt

class BatteryUsageAdapter(
    private val context: Context,
    private val battery:MutableList<BatteryModel> ,
    private val totalTime:Long
    ) :RecyclerView.Adapter<BatteryUsageAdapter.ViewHolder>() {

    private var batteryFinalList:MutableList<BatteryModel> = ArrayList()
    init {
         batteryFinalList = calcBatteryUsage(battery)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int):BatteryUsageAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.item_battery_usage,parent,false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder:BatteryUsageAdapter.ViewHolder,position: Int) {
        holder.txtAppName.text = getAppName(batteryFinalList[position].packageName.toString())
        holder.txtTimeUsage.text = "${batteryFinalList[position].timeUsage}"
        holder.txtPercent.text = batteryFinalList[position].percentusage.toString() + " %"
        holder.progressBar.progress = batteryFinalList[position].percentusage
        holder.imageView.setImageDrawable(getAppIcon(batteryFinalList[position].packageName.toString()))
    }

    override fun getItemCount(): Int {
       return batteryFinalList.size
    }

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        var txtAppName:TextView = view.findViewById(R.id.txt_App_Name)
        var txtPercent:TextView = view.findViewById(R.id.txtPercent)
        var txtTimeUsage:TextView = view.findViewById(R.id.txt_Time)
        var progressBar:ProgressBar = view.findViewById(R.id.progressBarPercent)
        var imageView:ImageView = view.findViewById(R.id.imageView)

    }

     fun calcBatteryUsage(batteryPercetArray:MutableList<BatteryModel>):MutableList<BatteryModel>{

         val finalList:MutableList<BatteryModel> = ArrayList()
         var sortedList = batteryPercetArray.groupBy { it.packageName }
            .mapValues { entry -> entry.value.sumBy { it.percentusage } }.toList()
            .sortedWith(compareBy{it.second}).reversed()


        for(item in sortedList){
            val bm = BatteryModel()

            val timePerApp = (item.second.toFloat() / 100) * totalTime.toFloat() / 1000 / 60
            val hour = timePerApp / 60
            val min = timePerApp % 60

            bm.packageName = item.first
            bm.percentusage = item.second
            bm.timeUsage = "${hour.roundToInt()} hour ${min.roundToInt()} minute"
            finalList += bm

        }
         return finalList
    }

    fun getAppName(PackageName: String):String{
        val pm = context.applicationContext.packageManager
        val ai : ApplicationInfo?= try{
            pm.getApplicationInfo(PackageName,0)
        }catch (e:PackageManager.NameNotFoundException){
            null
        }
        return (if(ai!= null) pm.getApplicationLabel(ai) else "{Unknown}") as String
    }

    fun getAppIcon(PackageName: String) :Drawable?{
        var icon : Drawable? = null
        icon = context.packageManager.getApplicationIcon(PackageName)
        try {
            icon = context.packageManager.getApplicationIcon(PackageName)
        }catch (e:PackageManager.NameNotFoundException){
            e.printStackTrace()
        }
        return icon
    }

}