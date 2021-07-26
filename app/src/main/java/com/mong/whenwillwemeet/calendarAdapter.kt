package com.mong.whenwillwemeet

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class calendarAdapter(private val dataSet: Vector<Calendar>, private val selDay : SelectDayActivity) : // 2차원 월 데이터
    RecyclerView.Adapter<calendarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var linLayout : LinearLayout
        var yearTV: TextView
        var dayTV: TextView
        var checkBox: CheckBox

        init {
            linLayout = view.findViewById(R.id.row_cal_ll)
            yearTV = view.findViewById(R.id.row_cal_yearTV)
            dayTV = view.findViewById(R.id.row_cal_dayTV)
            checkBox = view.findViewById(R.id.row_cal_checkBox)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_calendar, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val nowCal : Calendar = dataSet.get(position)

        viewHolder.yearTV.setText("${nowCal.get(Calendar.YEAR)}년 ${nowCal.get(Calendar.MONTH) + 1}월")
        viewHolder.dayTV.setText("${nowCal.get(Calendar.DATE)}일 " +
                "(${dateClass.dayofweek[nowCal.get(Calendar.DAY_OF_WEEK) - 1]})")

        viewHolder.linLayout.setOnClickListener {
            selDay.onClickDate(nowCal, viewHolder.checkBox)
        }
    }

    override fun getItemCount() = dataSet.size

}