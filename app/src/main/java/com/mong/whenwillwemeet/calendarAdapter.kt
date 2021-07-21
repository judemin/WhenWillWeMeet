package com.mong.whenwillwemeet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class calendarAdapter(private val dataSet: Vector<Calendar>) : // 2차원 월 데이터
    RecyclerView.Adapter<calendarAdapter.ViewHolder>() {

    val dayofweek = arrayListOf<String>("일","월","화","수","목","금","토")

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var yearTV: TextView
        lateinit var dayTV: TextView
        lateinit var checkBox: CheckBox

        init {
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
                "(${dayofweek[nowCal.get(Calendar.DAY_OF_WEEK) - 1]})")
    }

    override fun getItemCount() = dataSet.size

}