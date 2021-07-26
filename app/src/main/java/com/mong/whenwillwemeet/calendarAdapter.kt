package com.mong.whenwillwemeet

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.coroutines.coroutineContext

class calendarAdapter(private val selDay : SelectDayActivity) : // 2차원 월 데이터
    RecyclerView.Adapter<calendarAdapter.ViewHolder>() {

    val dataSet: Vector<dateClass> = Vector()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var linLayout : LinearLayout = view.findViewById(R.id.row_cal_ll)
        var yearTV: TextView = view.findViewById(R.id.row_cal_yearTV)
        var dayTV: TextView = view.findViewById(R.id.row_cal_dayTV)
        var checkBox: CheckBox = view.findViewById(R.id.row_cal_checkBox)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_calendar, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val nowDay : dateClass = dataSet[position]

        viewHolder.yearTV.setText("${nowDay.year}년 ${nowDay.month + 1}월")
        viewHolder.dayTV.setText("${nowDay.day}일 " +
                "(${dateClass.dayofweek[nowDay.dayOfWeek - 1]})")

        viewHolder.linLayout.setOnClickListener {
            val checkB = viewHolder.checkBox
            val state = checkB.isChecked

            selDay.onClickDate(nowDay, state)
            if(!state) {
                viewHolder.checkBox.performClick()
                checkB.buttonTintList = getColorStateList(selDay.applicationContext, R.color.baseBlue)
            } else if(state) {
                viewHolder.checkBox.performClick()
                checkB.buttonTintList = getColorStateList(selDay.applicationContext, R.color.pastelRed)
            }
        }
    }

    fun addData(tmp : dateClass){
        dataSet.add(tmp)
        notifyItemInserted(dataSet.size - 1)
    }

    override fun getItemCount() = dataSet.size

}